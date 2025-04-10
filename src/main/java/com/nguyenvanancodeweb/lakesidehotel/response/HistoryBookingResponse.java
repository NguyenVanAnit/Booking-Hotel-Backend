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
    private User user;
    private LocalDate checkin;
    private LocalDate checkout;
    private BigDecimal price;
    private Room room;
    private int status;


    public HistoryBookingResponse(HistoryBooking historyBooking) {
        this.id = historyBooking.getId();
        this.user = historyBooking.getUser();
        this.checkin = historyBooking.getCheckin();
        this.checkout = historyBooking.getCheckout();
        this.price = historyBooking.getPrice();
        this.status = historyBooking.getStatus();
        this.room = historyBooking.getRoom();
    }
}
