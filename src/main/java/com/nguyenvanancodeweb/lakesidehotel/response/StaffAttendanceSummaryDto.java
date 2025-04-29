package com.nguyenvanancodeweb.lakesidehotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAttendanceSummaryDto {
    private Long staffId; private String fullName; private String email; private String phoneNumber; private String department;
    private int daysPresent;  // số ngày có mặt (isAbsent = false)
    private int daysAbsent;
    private BigDecimal totalSalary;
}
