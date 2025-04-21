package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.PaymentConfirmationRequest;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    BookedRoom saveBooking(Long roomId, BookingRequest bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);

    void acceptBooking(Long bookingId);

    void rejectBooking(Long bookingId);

    BookedRoom getBookedRoomById(Long bookingId);

    void updateBookingStatus(Long bookingId, int status);

    void saveTxnRef(String TxnRef, Long bookingId);

    Boolean confirmPayment(PaymentConfirmationRequest confirmationRequest);

    BookedRoom getBookingByTxnRef(String txnRef);
}
