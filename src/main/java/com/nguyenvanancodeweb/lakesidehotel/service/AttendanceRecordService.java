package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.AttendanceRecord;
import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.repository.AttendanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService implements IAttendanceRecordService{
    private final AttendanceRecordRepository recordRepo;

    public AttendanceRecord checkIn(Long staffId) {
        LocalDate today = LocalDate.now();
        AttendanceRecord record = recordRepo.findByStaffIdAndDateBetween(staffId, today, today)
                .stream().findFirst().orElse(new AttendanceRecord());

        record.setStaffId(staffId);
        record.setDate(today);
        record.setCheckInTime(LocalTime.now());
        record.setIsAbsent(false);
        return recordRepo.save(record);
    }

    public AttendanceRecord checkOut(Long staffId) {
        LocalDate today = LocalDate.now();
        AttendanceRecord record = recordRepo.findByStaffIdAndDateBetween(staffId, today, today)
                .stream().findFirst().orElseThrow(() -> new RuntimeException("Chưa check-in"));

        record.setCheckOutTime(LocalTime.now());
        return recordRepo.save(record);
    }

    public List<Long> getStaffCheckedInToday() {
        return recordRepo.findStaffIdsCheckedInToday(LocalDate.now());
    }

    public List<Long> getStaffCheckedOutToday() {
        return recordRepo.findStaffIdsCheckedOutToday(LocalDate.now());
    }


    @Override
    public Map<String, Long> getMonthlyStats(Long staffId, YearMonth month) {
        return Map.of();
    }

    @Override
    public Map<String, Object> getAttendanceSummary(Long staffId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        long workingDays = recordRepo.countWorkingDays(staffId, start, end);
        List<LocalDate> absentDates = recordRepo.findAbsentDates(staffId, start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("workingDays", workingDays);
        result.put("absentDays", absentDates.size());
        result.put("absentDates", absentDates);
        return result;
    }

}
