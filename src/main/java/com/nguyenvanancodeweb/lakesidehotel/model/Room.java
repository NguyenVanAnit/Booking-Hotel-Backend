package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;

    private String name;
    private String description;
    private int floor;
    private Double pricePerDate;
    private int state;
    private int maxNumberAdult;
    private int maxNumberChildren;
    private int maxNumberPeople;
    private int ageLimit;
    private int numberBed;



    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room(){
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedRoom booking){
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
//        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10); //Tạo chuỗi code ngẫu nhiên khi đặt phòng gồm 10 ký tự
        booking.setBookingConfirmationCode(bookingCode);
    }


}
