package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long staffId;

    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private Boolean isAbsent;
    private String absentReason;

    private Integer status; // 0: chờ duyệt, 1: hợp lệ, 2: bất thường

    // Helper method
    public boolean isLate(LocalTime expectedCheckIn) {
        return checkInTime != null && checkInTime.isAfter(expectedCheckIn);
    }
}
