package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.RoomTask;
import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.Task;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;

    private Long roomId;
    private String roomName;
    private Long staffId;
    private String staffName;

    private Integer taskType; // 0 - Dọn dẹp | 1 - Bảo trì | 2 - Kiểm tra
    private Integer status; // 0 - Chưa làm | 1 - Đang làm | 2 - Hoàn thành | 3 - Bị huỷ

    private LocalDate assignedDate;
    private LocalDate completedDate;

    private String notes;

    public TaskDto(RoomTask task) {
        this.id = task.getId();
        this.roomId = task.getRoom().getId();
        this.roomName = task.getRoom().getName();
        this.staffId = task.getStaff().getId();
        this.staffName = task.getStaff().getFullName();
        this.taskType = task.getTaskType();
        this.status = task.getStatus();
        this.assignedDate = task.getAssignedDate();
        this.completedDate = task.getCompletedDate();
        this.notes = task.getNotes();
    }
}
