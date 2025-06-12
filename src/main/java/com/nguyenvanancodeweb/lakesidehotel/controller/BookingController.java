package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.PaymentConfirmationRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingDetailDto;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.RoomRevenueDto;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.IBookingService;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoomService;
import com.nguyenvanancodeweb.lakesidehotel.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
//@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final VNPAYService vnpayService;

//    @GetMapping("/confirmation/{confirmationCode}")
//    public ResponseEntity<ApiResponseDTO<?>> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
//        try{
//            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
//            BookingResponse bookingResponse = getBookingResponse(booking);
//            return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
//                    null, bookingResponse
//            )));
//        }catch(ResourceNotFoundException ex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(false, ex.getMessage()));
//        }
//    }

    @GetMapping("/all-bookings")
    public ResponseEntity<ApiResponseDTO<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookingResponses = bookingService.getAllBookingResponses();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Lấy danh sách đặt phòng thành công",
                new DataResponseDTO<>(bookingResponses.size(), bookingResponses)));
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<ApiResponseDTO<String>> saveBooking(@PathVariable Long roomId,
                                                              @RequestBody BookingRequest bookingRequest,
                              HttpServletRequest request) {
        try{
            BookedRoom booking = bookingService.saveBooking(roomId, bookingRequest);
            BigDecimal totalPrice = booking.getTotalPrice();
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnpayService.createOrder(request, totalPrice.intValue(),
                    booking.getBookingId().toString(), baseUrl);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Tạo lệnh đặt phòng thành công",
                    new DataResponseDTO<>(null, vnpayUrl)));
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/room/{roomId}/booking-by-letan")
    public ResponseEntity<ApiResponseDTO<Void>> saveBookingByLetan(@PathVariable Long roomId,
                                                              @RequestBody BookingRequest bookingRequest,
                                                              HttpServletRequest request) {
        try{
            bookingService.saveBookingByLetan(roomId, bookingRequest);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Tạo lệnh đặt phòng thành công"));
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
        }
    }

//    @GetMapping("/vnpay-payment-return")
//    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int result = vnpayService.orderReturn(request);
//
//        String redirectUrl;
//        if (result == 1) {
//            // Thành công
//            redirectUrl = "http://localhost:5173/payment-result?status=success";
//        } else if (result == 0) {
//            // Thanh toán thất bại
//            redirectUrl = "http://localhost:5173/payment-result?status=fail";
//        } else {
//            // Sai checksum
//            redirectUrl = "http://localhost:5173/payment-result?status=invalid";
//        }
//
//        response.sendRedirect(redirectUrl);
//    }

    @PostMapping("/payment/confirm")
    public ResponseEntity<ApiResponseDTO<BookingResponse>> confirmPayment(
            @RequestBody PaymentConfirmationRequest confirmationRequest){
        BookingResponse bookingResponse = bookingService.confirmPayment(confirmationRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, bookingResponse)));
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking (@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
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

    @GetMapping("/count/month")
    public ResponseEntity<ApiResponseDTO<Long>> getMonthlyBookingCount(
            @RequestParam Long roomId,
            @RequestParam int month,
            @RequestParam int year) {
        Long count = bookingService.getMonthlyBookingCount(roomId, month, year);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, count
        )));
    }

    @GetMapping("/count/year")
    public ResponseEntity<ApiResponseDTO<Long>> getYearlyBookingCount(
            @RequestParam Long roomId,
            @RequestParam int year) {
        Long count = bookingService.getYearlyBookingCount(roomId, year);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, count
        )));
    }

    @GetMapping("/statistics/room-revenue")
    public ResponseEntity<ApiResponseDTO<List<RoomRevenueDto>>> getRoomRevenue(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, bookingService.getRoomRevenue(month, year)
        )));
    }

    @GetMapping("/by-checkin-date")
    public ResponseEntity<ApiResponseDTO<List<BookingResponse>>> getBookingsByCheckInDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate
    ) {
        List<BookingResponse> bookings = bookingService.getBookingsByCheckInDate(checkInDate);

        DataResponseDTO<List<BookingResponse>> data = new DataResponseDTO<>(bookings.size(), bookings);

        return ResponseEntity.ok(new ApiResponseDTO<>(true,
                "Lấy danh sách booking theo ngày check-in thành công", data));
    }

    @PutMapping("/update-checked/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> updateBookingCheckIn(
            @PathVariable Long id,
            @RequestParam int status
    ) {
        bookingService.updateChecked(id, status);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200"));
    }

    @GetMapping("/detail-booking")
    public ResponseEntity<ApiResponseDTO<BookingDetailDto>> getBookingDetail(@RequestParam Long bookingId) {
        BookingDetailDto bookedRoom = bookingService.getDetailBooking(bookingId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, bookedRoom
        )));
    }

    @GetMapping("/find-by-code")
    public ResponseEntity<ApiResponseDTO<Long>> findBookingIdByCode(@RequestParam String code) {
        Long bookingId = bookingService.getBookingIdByConfirmationCode(code);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, bookingId)));
    }


//    @GetMapping("/history-booking")
//    public ResponseEntity<Page<BookingResponse>> getBookingsHistory() {
//
//    }
}
