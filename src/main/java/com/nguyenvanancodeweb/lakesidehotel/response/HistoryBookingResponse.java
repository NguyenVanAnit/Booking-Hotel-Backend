package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryBookingResponse {
    private Long id;
    private Long userId;
    private LocalDate checkin;
    private LocalDate checkout;
    private BigDecimal price;
    private Long roomId;
    private String roomName;
    private int status;
    private Long bookingId;
    private int isChecked;

    public HistoryBookingResponse(HistoryBooking historyBooking) {
        this.id = historyBooking.getId();
        this.userId = historyBooking.getUser().getId();
        this.checkin = historyBooking.getCheckin();
        this.checkout = historyBooking.getCheckout();
        this.price = historyBooking.getPrice();
        this.status = historyBooking.getStatus();
        this.roomId = historyBooking.getRoom().getId();
        this.roomName = historyBooking.getRoom().getName();
        this.bookingId = historyBooking.getBookingId();
        this.isChecked = historyBooking.getIsChecked();
    }
}
