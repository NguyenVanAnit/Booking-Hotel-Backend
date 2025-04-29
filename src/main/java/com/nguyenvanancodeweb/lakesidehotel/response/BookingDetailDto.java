package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.ServiceBooked;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailDto {
    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestEmail;

    private int NumOfAdults;

    private int NumOfChildren;

    private String bookingConfirmationCode;

    private String bookingTime;

    //    0 - Chờ thanh toán, 1 - Đã thanh toán thành công, 2 - Không thành công, 3 - Đã hủy, 4 - Đã từ chối
    private int status;

    private String phoneNumber;

    private String transactionCode;

    private String bank;

    private BigDecimal totalPrice;

    private Long roomId;

    private Long userId;

    private List<ServiceBookedDto> serviceBookedDtos;

    private String txnRef;

    // 0 chua checkin, 1 da checkin, 2 da checkou   t
    private Integer isChecked;

    private String fullName;
    private String email;
    private String phoneNumberUser;
}
