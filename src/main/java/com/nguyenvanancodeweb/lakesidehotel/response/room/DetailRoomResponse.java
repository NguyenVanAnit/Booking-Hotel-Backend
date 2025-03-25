package com.nguyenvanancodeweb.lakesidehotel.response.room;

import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class DetailRoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;

    private String name;
    private String description;
    private int floor;
    private int state;
    private int maxNumberAdult;
    private int maxNumberChildren;
    private int maxNumberPeople;
    private int ageLimit;
    private int numberBed;

    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String photo5;

//    private List<BookingResponse> bookings;
//    private List<HistoryBooking> historyBookings;
    private List<Rate> rates;
    private List<String> services;

    public DetailRoomResponse(Room room) {
        this.id = room.getId();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
        this.isBooked = room.isBooked();
        this.name = room.getName();
        this.description = room.getDescription();
        this.floor = room.getFloor();
        this.state = room.getState();
        this.maxNumberAdult = room.getMaxNumberAdult();
        this.maxNumberChildren = room.getMaxNumberChildren();
        this.maxNumberPeople = room.getMaxNumberPeople();
        this.ageLimit = room.getAgeLimit();
        this.numberBed = room.getNumberBed();
    }
}
