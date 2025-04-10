package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Boolean isActive;
    private BigDecimal priceService;
    private String serviceType;
    private Boolean isFree;
    private int maxQuantity;

    @OneToMany(mappedBy = "services", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ServiceBooked> serviceBookeds;

    @ManyToMany(mappedBy = "services")
    private Collection<Room> rooms = new HashSet<>();

}
