package com.nguyenvanancodeweb.lakesidehotel.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceBookedRequest {
    private Long serviceId;
    private int quantity;
    private BigDecimal totalPrice;
}
