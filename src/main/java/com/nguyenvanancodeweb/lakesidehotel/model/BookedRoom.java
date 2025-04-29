package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_In")
    private LocalDate checkInDate;

    @Column(name = "check_Out")
    private LocalDate checkOutDate;

    @Column(name = "guest_Email")
    private String guestEmail;

    @Column(name = "adults")
    private int NumOfAdults;

    @Column(name = "children")
    private int NumOfChildren;

    @Setter
    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    @Column(name = "booking_time")
    private String bookingTime;

//    0 - Chờ thanh toán, 1 - Đã thanh toán thành công, 2 - Không thành công, 3 - Đã hủy, 4 - Đã từ chối
//    @Column(name = "status", nullable = false)
    private int status = 0;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "bank")
    private String bank;

    private BigDecimal totalPrice;

    @OneToOne(mappedBy = "bookedRoom", cascade = CascadeType.ALL)
    private Rate rate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "bookedRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ServiceBooked> serviceBookeds;

    private String txnRef;

    // 0 chua checkin, 1 da checkin, 2 da checkout
    private Integer isChecked;

}
