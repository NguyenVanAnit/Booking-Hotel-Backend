package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RoomRequest {
    private String name;
    private String description;
    private String roomType;
    private BigDecimal roomPrice;
    private int floor;
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
}
