package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import org.springframework.data.domain.Page;

public interface IHistoryBookingService {
    HistoryBooking addHistoryBooking(BookedRoom bookedRoom);

    Page<HistoryBooking> getHistoryBooking(int pageNumber, int pageSize, Long userId, Long roomId);
}
