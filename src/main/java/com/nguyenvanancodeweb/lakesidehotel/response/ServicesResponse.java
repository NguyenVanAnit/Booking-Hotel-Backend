package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesResponse {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private BigDecimal priceService;
    private String serviceType;
    private boolean isFree;
    private int maxQuantity;

    public ServicesResponse(Services services) {
        this.id = services.getId();
        this.name = services.getName();
        this.description = services.getDescription();
        this.isActive = services.getIsActive();
        this.priceService = services.getPriceService();
        this.serviceType = services.getServiceType();
        this.isFree = services.getIsFree();
        this.maxQuantity = services.getMaxQuantity();
    }
}
