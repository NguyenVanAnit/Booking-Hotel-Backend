package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookingRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestEmail;
    private int numOfAdults;
    private int numOfChildren;
    private String phoneNumberOther;
    private Long roomId;
    private Long userId;
//    private BigDecimal totalPrice;
    private List<ServiceBookedRequest> serviceBookedRequests;
}
