package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidPaginationException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.repository.HistoryBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryBookingService implements IHistoryBookingService {
    private final HistoryBookingRepository historyBookingRepository;
    private final IUserService userService;

    @Override
    public HistoryBooking addHistoryBooking(BookedRoom bookedRoom) {
        User user = userService.getUserByUserId(bookedRoom.getUserId());

        HistoryBooking historyBooking = new HistoryBooking();
        historyBooking.setBookingId(bookedRoom.getBookingId());
        historyBooking.setCheckin(bookedRoom.getCheckInDate());
        historyBooking.setCheckout(bookedRoom.getCheckOutDate());
        historyBooking.setStatus(bookedRoom.getStatus());
        historyBooking.setPrice(bookedRoom.getTotalPrice());
        historyBooking.setUser(user);
        historyBooking.setRoom(bookedRoom.getRoom());
        historyBooking.setIsChecked(bookedRoom.getIsChecked());
        return historyBookingRepository.save(historyBooking);
    }

    @Override
    public Page<HistoryBooking> getHistoryBooking(int pageNumber, int pageSize, Long userId, Long roomId) {
        if(pageNumber < 0 || pageSize < 1){
            throw new InvalidPaginationException("Số trang hoặc kích thước trang không hợp lệ!");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkin").descending());
        if(userId != null) {
            return historyBookingRepository.findByUserId(userId, pageable);
        }
        if(roomId != null) {
            return historyBookingRepository.findByRoomId(roomId, pageable);
        }
        return historyBookingRepository.findAll(pageable);
    }

    @Override
    public HistoryBooking getHistoryBookingByBooking(Long bookingId) {
        return historyBookingRepository.findByBookingIdCustom(bookingId);
    }

    @Override
    public void updateStatusHistoryBooking(Long bookingId) {
        HistoryBooking historyBooking = getHistoryBookingByBooking(bookingId);
        historyBooking.setStatus(1);
        historyBookingRepository.save(historyBooking);
    }

    @Override
    public void updateIsCheckedHistoryBooking(Long bookingId, int isChecked) {
        HistoryBooking historyBooking = getHistoryBookingByBooking(bookingId);
        historyBooking.setIsChecked(isChecked);
        historyBookingRepository.save(historyBooking);
    }


}
