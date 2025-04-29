package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.AttendanceRecord;
import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.repository.AttendanceRecordRepository;
import com.nguyenvanancodeweb.lakesidehotel.response.StaffAttendanceSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService implements IAttendanceRecordService{
    private final AttendanceRecordRepository recordRepo;
    private final IStaffService staffService;

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

    @Override
    public List<StaffAttendanceSummaryDto> getStaffAttendanceSummary(int month, int year) {
        List<Staff> staffList = staffService.getAllStaff();

        return staffList.stream()
                .map(staff -> {
                    int present = recordRepo.countPresentDays(staff.getId(), month, year);
                    int absent = recordRepo.countAbsentDays(staff.getId(), month, year);

                    BigDecimal salary = staff.getSalary();
                    BigDecimal totalSalary = salary.multiply(BigDecimal.valueOf(present));

                    StaffAttendanceSummaryDto dto = new StaffAttendanceSummaryDto();
                    dto.setStaffId(staff.getId());
                    dto.setFullName(staff.getFullName());
                    dto.setEmail(staff.getEmail());
                    dto.setPhoneNumber(staff.getPhoneNumber());
                    dto.setDepartment(staff.getDepartment());
                    dto.setDaysPresent(present);
                    dto.setDaysAbsent(absent);
                    dto.setTotalSalary(totalSalary);

                    return dto;
                })
                .collect(Collectors.toList());
    }

}
