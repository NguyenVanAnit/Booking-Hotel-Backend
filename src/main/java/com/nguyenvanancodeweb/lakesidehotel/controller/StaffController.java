package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.service.IStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<String>> createStaff(@RequestBody Staff staff) {
        staffService.addStaff(staff);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, "Thêm nhân viên thành công")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> updateStaff(@PathVariable Long id,
                                                              @RequestBody Staff updatedStaff) {
        staffService.updateStaff(id, updatedStaff);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, "Cập nhật nhân viên thành công")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, "Xoá nhân viên thành công")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Staff>> getStaffById(@PathVariable Long id) {
        Staff staff = staffService.getStaffById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, staff)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Staff>>> getAllStaff() {
        List<Staff> all = staffService.getAllStaff();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, all)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<Staff>>> searchByName(@RequestParam String name) {
        List<Staff> result = staffService.searchByName(name);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, result)));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ApiResponseDTO<List<Staff>>> getByRole(@PathVariable Integer role) {
        List<Staff> result = staffService.getByRole(role);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, result)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponseDTO<List<Staff>>> getByStatus(@PathVariable Integer status) {
        List<Staff> result = staffService.getByStatus(status);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, result)));
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<ApiResponseDTO<Long>> countByRole(@PathVariable Integer role) {
        long count = staffService.countByRole(role);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, count)));
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<ApiResponseDTO<Long>> countByStatus(@PathVariable Integer status) {
        long count = staffService.countByStatus(status);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200",
                new DataResponseDTO<>(null, count)));
    }
}
