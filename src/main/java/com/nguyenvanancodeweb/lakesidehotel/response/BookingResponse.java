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

    private int NumOfChildren;

    private int NumOfAdults;

    private String bookingConfirmationCode;

    private String bookingTime;

    private String guestName;

    private String roomName;

    private String phoneNumber;

    private String email;

    private int isChecked;

}
