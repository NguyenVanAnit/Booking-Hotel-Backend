package com.nguyenvanancodeweb.lakesidehotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBookedDto {
    private Long id;
    private String serviceName;
    private int quantity;
    private BigDecimal totalPrice;
}
