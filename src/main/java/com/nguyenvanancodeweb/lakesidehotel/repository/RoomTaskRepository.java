package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.RoomTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTaskRepository extends JpaRepository<RoomTask, Long> {
    List<RoomTask> findByRoomId(Long roomId);
    List<RoomTask> findByStaffId(Long staffId);
}
