package com.nguyenvanancodeweb.lakesidehotel.response.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Base64;
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
    private Double totalRating;
    private String photo1;

    public AllRoomResponse(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.description = room.getDescription();
        this.roomType = room.getRoomType();
        this.roomPrice = room.getRoomPrice();
        this.totalRating = room.getTotalRating();
        this.photo1 = blobToBase64(room.getPhoto1());
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
