package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);

    void acceptBooking(Long bookingId);

    void rejectBooking(Long bookingId);
}
