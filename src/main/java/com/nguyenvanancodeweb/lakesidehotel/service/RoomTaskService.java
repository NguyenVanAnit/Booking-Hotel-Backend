package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.RoomTask;
import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.repository.RoomRepository;
import com.nguyenvanancodeweb.lakesidehotel.repository.RoomTaskRepository;
import com.nguyenvanancodeweb.lakesidehotel.repository.StaffRepository;
import com.nguyenvanancodeweb.lakesidehotel.response.TaskDto;
import com.nguyenvanancodeweb.lakesidehotel.response.ThongkeTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTaskService {

    private final RoomTaskRepository roomTaskRepository;
    private final StaffRepository staffRepository;
    private final RoomRepository roomRepository;

    public RoomTask assignTask(Long roomId, Long staffId, Integer taskType, String notes) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        RoomTask task = new RoomTask();
        task.setRoom(room);
        task.setStaff(staff);
        task.setTaskType(taskType);
        task.setStatus(0); // 0 = chưa làm
        task.setAssignedDate(LocalDate.now());
        task.setNotes(notes);

        return roomTaskRepository.save(task);
    }

    public List<TaskDto> getTasksByRoom(Long roomId) {
        List<RoomTask> tasks = roomTaskRepository.findByRoomId(roomId);
        return tasks.stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasksByStaff(Long staffId) {
        List<RoomTask> tasks = roomTaskRepository.findByStaffId(staffId);
        return tasks.stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    public RoomTask updateTaskStatus(Long taskId, Integer status) {
        RoomTask task = roomTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        if (status == 1) { // 1 = Hoàn thành
            task.setCompletedDate(LocalDate.now());
        }

        return roomTaskRepository.save(task);
    }

    public List<ThongkeTaskDto> getStaffTaskStatsByMonth(int month, int year) {
        return roomTaskRepository.getTaskStatsForAllStaffByMonth(month, year);
    }
}
