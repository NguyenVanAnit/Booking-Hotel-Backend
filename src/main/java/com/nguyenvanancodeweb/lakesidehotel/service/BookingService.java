package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    public List<BookedRoom> getAllRoomsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Ngày nhận phòng phải trước ngày trả phòng");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBooking = room.getBookings();
        Boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBooking);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else {
            throw new InvalidBookingRequestException("Xin lỗi, căn phòng không trống vào ngày được chọn");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public void acceptBooking(Long bookingId) {
        BookedRoom booking = bookingRepository.findById(bookingId).get();
        booking.setStatus(1);
        bookingRepository.save(booking);

        // Lấy tất cả các đơn đặt phòng cùng phòng
        List<BookedRoom> roomBookings = bookingRepository.findByRoomId(booking.getRoom().getId());

        // Hủy các đơn chưa được duyệt (status == 0) và trùng ngày
        roomBookings.stream()
                .filter(existingBooking -> existingBooking.getStatus() == 0)
                .filter(existingBooking ->
                        existingBooking.getCheckInDate().isBefore(booking.getCheckOutDate()) &&
                                existingBooking.getCheckOutDate().isAfter(booking.getCheckInDate()))
                .forEach(existingBooking -> {
                    existingBooking.setStatus(2);
                    bookingRepository.save(existingBooking);
                });
    }

    @Override
    public void rejectBooking(Long bookingId) {
        BookedRoom booking = bookingRepository.findById(bookingId).get();
        booking.setStatus(2);
        bookingRepository.save(booking);
    }

//    @Override
//    public Page<BookedRoom> getHistoryBooking(int pageNumber, int pageSize, Long roomId, Long userId){
//
//    }

    private Boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .filter(existingBooking -> existingBooking.getStatus() == 1)
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

}
