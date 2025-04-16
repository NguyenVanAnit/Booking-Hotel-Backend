package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    private Integer role; // 0: nhan vien, 1: to truong, 2: truong phong
    private LocalDate hireDate;
    private BigDecimal salary;
    private String department;
    private Integer gender;
    private LocalDate birthDate;

    private Integer status; // 1: dang lam viec, 2: nghi viec, 3: bi duoi viec
}
