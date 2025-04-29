package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.PaymentConfirmationRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingDetailDto;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.RoomRevenueDto;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    BookedRoom saveBooking(Long roomId, BookingRequest bookingRequest);

    void saveBookingByLetan(Long roomId, BookingRequest bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();
    List<BookingResponse> getAllBookingResponses();

    List<BookedRoom> getBookingsByUserEmail(String email);

    List<BookedRoom> getAllRoomsByRoomId(Long roomId);

    void acceptBooking(Long bookingId);

    void rejectBooking(Long bookingId);

    BookedRoom getBookedRoomById(Long bookingId);

    void updateBookingStatus(Long bookingId, int status);

    void saveTxnRef(String TxnRef, Long bookingId);

    BookingResponse confirmPayment(PaymentConfirmationRequest confirmationRequest);

    BookedRoom getBookingByTxnRef(String txnRef);

    Long getMonthlyBookingCount(Long roomId, int month, int year);
    Long getYearlyBookingCount(Long roomId, int year);
    List<RoomRevenueDto> getRoomRevenue(Integer month, Integer year);

    List<BookingResponse> getBookingsByCheckInDate(LocalDate checkInDate);

    void updateChecked(Long bookingId, int status);

    BookingDetailDto getDetailBooking(Long bookingId);

    Long getBookingIdByConfirmationCode(String confirmationCode);
}
