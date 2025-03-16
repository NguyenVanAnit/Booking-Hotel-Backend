package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ServiceBooked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booked_id", nullable = false)
    private BookedRoom bookedRoom;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services services;

    private int quantity;
    private BigDecimal totalPrice;


}
