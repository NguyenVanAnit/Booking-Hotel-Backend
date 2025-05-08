package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.response.HistoryBookingResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IHistoryBookingService {
    HistoryBooking addHistoryBooking(BookedRoom bookedRoom);

    Page<HistoryBooking> getHistoryBooking(int pageNumber, int pageSize, Long userId, Long roomId);

    HistoryBooking getHistoryBookingByBooking(Long bookingId);

    void updateStatusHistoryBooking(Long bookingId);

    void updateIsCheckedHistoryBooking(Long bookingId, int isChecked);

    List<HistoryBookingResponse> findHistoryBookingByCheckoutDate(LocalDate checkout);
}
