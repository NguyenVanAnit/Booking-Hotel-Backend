package com.nguyenvanancodeweb.lakesidehotel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Services {
    private String name;
    private String description;
    private boolean isActive;
    private double priceService;
    private String serviceType;
    private boolean isFree;


}
