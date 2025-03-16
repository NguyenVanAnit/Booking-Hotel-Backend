package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "guest_FullName")
    private String guestFullName;

    @Column(name = "guest_Email")
    private String guestEmail;

    @Column(name = "adults")
    private int NumOfAdults;

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    @Column(name = "booking_time")
    private String bookingTime;

    @Column(name = "status", nullable = false)
    private int status = 0;

    @Column(name = "account_bank")
    private String accountBank;

    @Column(name = "name_user_bank")
    private String nameUserBank;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "bank")
    private String bank;

    @OneToOne(mappedBy = "bookedRoom", cascade = CascadeType.ALL)
    private Rate rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "bookedRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ServiceBooked> serviceBookeds;

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
