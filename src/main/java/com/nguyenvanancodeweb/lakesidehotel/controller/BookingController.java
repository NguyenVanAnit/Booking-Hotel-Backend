package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.IBookingService;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;

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
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
        try{
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Mã đặt phòng của bạn là: "+confirmationCode);
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        AllRoomResponse room = new AllRoomResponse(theRoom);

        return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getGuestFullName(),
                booking.getGuestEmail(), booking.getNumOfAdults(),
                booking.getBookingConfirmationCode(), booking.getBookingTime(),
                booking.getStatus(), booking.getAccountBank(),
                booking.getNameUserBank(), booking.getPhoneNumber(),
                booking.getTransactionCode(), booking.getBank(),
                room);
    }
}
