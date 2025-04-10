package com.nguyenvanancodeweb.lakesidehotel.response.room;

import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Base64;
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
        this.photo1 = blobToBase64(room.getPhoto1());
        this.photo2 = blobToBase64(room.getPhoto2());
        this.photo3 = blobToBase64(room.getPhoto3());
        this.photo4 = blobToBase64(room.getPhoto4());
        this.photo5 = blobToBase64(room.getPhoto5());
    }

    private String blobToBase64(Blob blob) {
        if (blob == null) return null;
        try (InputStream is = blob.getBinaryStream()) {
            byte[] bytes = is.readAllBytes();
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
