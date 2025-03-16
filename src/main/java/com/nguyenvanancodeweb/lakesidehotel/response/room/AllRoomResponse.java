package com.nguyenvanancodeweb.lakesidehotel.response.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String name;
    private String description;

    public AllRoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.description = room.getDescription();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
//        this.photo1
    }

//    public AllRoomResponse(Long id, String roomType, BigDecimal roomPrice) {
//
//    }

//    public RoomResponse(Long id,
//                        String roomType,
//                        BigDecimal roomPrice,
//                        boolean isBooked,
//                        byte[] photoBytes
////                        List<BookingResponse> bookings
//    ) {
//        this.id = id;
//        this.roomType = roomType;
//        this.roomPrice = roomPrice;
//        this.isBooked = isBooked;
//        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
////        this.bookings = bookings;
//    }

//    private String convertBlobToBa
}
