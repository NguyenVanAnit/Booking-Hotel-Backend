package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.AttendanceRecord;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface IAttendanceRecordService {
    AttendanceRecord checkIn(Long staffId);
    AttendanceRecord checkOut(Long staffId);
    List<Long> getStaffCheckedInToday();
    List<Long> getStaffCheckedOutToday();
    Map<String, Long> getMonthlyStats(Long staffId, YearMonth month);
    Map<String, Object> getAttendanceSummary(Long staffId, int year, int month);
}
