package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    private int NumOfAdults;

    private String bookingConfirmationCode;

    private String bookingTime;

    private int status;

    private String accountBank;

    private String nameUserBank;

    private String phoneNumber;

    private String transactionCode;

    private String bank;

    private AllRoomResponse room;

    public BookingResponse(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode, String bookingTime) {
        this.bookingId = bookingId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.bookingTime = bookingTime;
    }
}
