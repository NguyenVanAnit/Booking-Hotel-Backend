package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.IBookingService;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoomService;
import com.nguyenvanancodeweb.lakesidehotel.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final VNPAYService vnpayService;

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try{
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking: bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @PostMapping("/room/{roomId}/booking")
    public String saveBooking(@PathVariable Long roomId, @RequestBody BookingRequest bookingRequest,
                              HttpServletRequest request) {
        try{
//            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            BookedRoom booking = bookingService.saveBooking(roomId, bookingRequest);
            BigDecimal totalPrice = booking.getTotalPrice();
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnpayService.createOrder(request, totalPrice.intValue(),
                    booking.getBookingId().toString(), baseUrl);
//            String paymentUrl = vnpayService.createPaymentUrl(bookingRequest.getBookingId(), totalPrice.doubleValue());
//            DataResponseDTO<String> dataResponseDTO = new DataResponseDTO<>(null, paymentUrl);
            return "redirect:" + vnpayUrl;
        }catch (InvalidBookingRequestException e){
            return e.getMessage();
        }
    }

    @GetMapping("/vnpay-payment-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int result = vnpayService.orderReturn(request);

        String redirectUrl;
        if (result == 1) {
            // Thành công
            redirectUrl = "http://localhost:3000/payment-result?status=success";
        } else if (result == 0) {
            // Thanh toán thất bại
            redirectUrl = "http://localhost:3000/payment-result?status=fail";
        } else {
            // Sai checksum
            redirectUrl = "http://localhost:3000/payment-result?status=invalid";
        }

        response.sendRedirect(redirectUrl);
    }


//    @GetMapping("/vnpay-payment-return")
//    public String paymentCompleted(HttpServletRequest request){
//        int paymentStatus = vnpayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
////        model.addAttribute("orderId", orderInfo);
////        model.addAttribute("totalPrice", totalPrice);
////        model.addAttribute("paymentTime", paymentTime);
////        model.addAttribute("transactionId", transactionId);
//
//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
//    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking (@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @PatchMapping("/booking/{bookingId}/accept")
    public ResponseEntity<?> acceptBooking (@PathVariable Long bookingId) {
        try{
            bookingService.acceptBooking(bookingId);
            return ResponseEntity.ok("Đơn đặt phòng đã được duyệt thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/booking/{bookingId}/reject")
    public ResponseEntity<?> rejectBooking (@PathVariable Long bookingId) {
        try{
            bookingService.rejectBooking(bookingId);
            return ResponseEntity.ok("Đơn đặt phòng đã được từ chối thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/history-booking")
//    public ResponseEntity<Page<BookingResponse>> getBookingsHistory() {
//
//    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId());
        AllRoomResponse room = new AllRoomResponse(theRoom);

        return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(), booking.getCheckOutDate(),
                booking.getStatus(), booking.getTotalPrice(), booking.getUserId(), booking.getRoom().getId());
    }
}
