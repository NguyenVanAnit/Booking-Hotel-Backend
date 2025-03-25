package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private int status;

    private BigDecimal totalPrice;

    private Long userId;

    private Long roomId;

//    private AllRoomResponse room;

    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode, String bookingTime) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
