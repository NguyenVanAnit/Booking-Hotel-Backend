package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.RoomTask;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.TaskDto;
import com.nguyenvanancodeweb.lakesidehotel.service.RoomTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class RoomTaskController {

    private final RoomTaskService roomTaskService;

    @PostMapping("/assign")
    public ResponseEntity<ApiResponseDTO<Void>> assignTask(
            @RequestParam Long roomId,
            @RequestParam Long staffId,
            @RequestParam Integer taskType,
            @RequestParam(required = false) String notes) {

        try {
            RoomTask task = roomTaskService.assignTask(roomId, staffId, taskType, notes);
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Giao việc thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(
                    false,
                    e.getMessage()
            ));
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponseDTO<List<TaskDto>>> getTasksByRoom(@PathVariable Long roomId) {
        List<TaskDto> tasks = roomTaskService.getTasksByRoom(roomId);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Lấy danh sách task theo phòng thành công",
                new DataResponseDTO<>( null, tasks )
        ));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<ApiResponseDTO<List<TaskDto>>> getTasksByStaff(@PathVariable Long staffId) {
        List<TaskDto> tasks = roomTaskService.getTasksByStaff(staffId);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Lấy danh sách task theo nhân viên thành công",
                new DataResponseDTO<>(null, tasks )
        ));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<ApiResponseDTO<Void>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam Integer status) {
        try {
            roomTaskService.updateTaskStatus(taskId, status);
            return ResponseEntity.ok(new ApiResponseDTO<>(
                    true,
                    "Cập nhật trạng thái task thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(
                    false,
                    e.getMessage()
            ));
        }
    }
}
