package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.service.IAttendanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceRecordController {
    private final IAttendanceRecordService attendanceService;

    @PostMapping("/check-in/{staffId}")
    public ResponseEntity<ApiResponseDTO<?>> checkIn(@PathVariable Long staffId) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, attendanceService.checkIn(staffId)
        )));
    }

    @PostMapping("/check-out/{staffId}")
    public ResponseEntity<ApiResponseDTO<?>> checkOut(@PathVariable Long staffId) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, attendanceService.checkOut(staffId)
        )));
    }

    @GetMapping("/checked-in-today")
    public ResponseEntity<?> getCheckedInStaffToday() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, attendanceService.getStaffCheckedInToday()
        )));
    }

    @GetMapping("/checked-out-today")
    public ResponseEntity<?> getCheckedOutStaffToday() {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, attendanceService.getStaffCheckedOutToday()
        )));
    }

    @GetMapping("/work-absent-days/{staffId}")
    public ResponseEntity<?> getWorkAndAbsentDates(
            @PathVariable Long staffId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", new DataResponseDTO<>(
                null, attendanceService.getAttendanceSummary(staffId, year, month)
        )));
    }
}
