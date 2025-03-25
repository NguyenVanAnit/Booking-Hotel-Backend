package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.repository.BookingRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final IHistoryBookingService historyBookingService;
    private final IServiceBookedService serviceBookedService;

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
    public String saveBooking(Long roomId, BookingRequest bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Ngày nhận phòng phải trước ngày trả phòng");
        }
        Room room = roomService.getRoomById(roomId);
        List<BookedRoom> existingBooking = room.getBookings();
        Boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBooking);
        if (roomIsAvailable) {
//            bookingRequest.setStatus(0);
            BookedRoom bookedRoom = new BookedRoom();
            bookedRoom.setCheckInDate(bookingRequest.getCheckInDate());
            bookedRoom.setCheckOutDate(bookingRequest.getCheckOutDate());
            bookedRoom.setGuestEmail(bookingRequest.getGuestEmail());
            bookedRoom.setNumOfAdults(bookingRequest.getNumOfAdults());
            bookedRoom.setNumOfChildren(bookingRequest.getNumOfChildren());
            bookedRoom.setPhoneNumber(bookingRequest.getPhoneNumber());
            bookedRoom.setTotalPrice(bookingRequest.getTotalPrice());
            bookedRoom.setUserId(bookingRequest.getUserId());
            bookedRoom.setRoom(room);
            bookedRoom.setStatus(0);
            bookedRoom.setBookingTime(String.valueOf(LocalDate.now()));

            serviceBookedService.savingServiceForBooking(bookedRoom);

            bookingRepository.save(bookedRoom);
            room.addBooking(bookedRoom);
            historyBookingService.addHistoryBooking(bookedRoom);

            return bookedRoom.getBookingConfirmationCode();
        }else {
            throw new InvalidBookingRequestException("Xin lỗi, căn phòng không trống vào ngày được chọn");
        }
    }

    @Override
    public void updateBookingStatus(Long bookingId, int status) {
        Optional<BookedRoom> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            BookedRoom booking = bookingOpt.get();
            booking.setStatus(status);
            bookingRepository.save(booking);
        } else {
            throw new InvalidBookingRequestException("Không tìm thấy đặt phòng với ID: " + bookingId);
        }
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

    @Override
    public BookedRoom getBookedRoomById(Long bookingId) {
        BookedRoom bookedRoom = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID " + bookingId));
        return bookedRoom;
    }


    private Boolean roomIsAvailable(BookingRequest bookingRequest, List<BookedRoom> existingBookings) {
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
