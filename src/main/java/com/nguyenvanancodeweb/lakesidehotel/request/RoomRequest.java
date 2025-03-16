package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

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
}
