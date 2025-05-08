package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.RoomTask;
import com.nguyenvanancodeweb.lakesidehotel.response.ThongkeTaskDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTaskRepository extends JpaRepository<RoomTask, Long> {
    List<RoomTask> findByRoomId(Long roomId);
    List<RoomTask> findByStaffId(Long staffId);

    @Query("""
        SELECT new com.nguyenvanancodeweb.lakesidehotel.response.ThongkeTaskDto(
            rt.staff.id,
            SUM(CASE WHEN rt.status = 1 THEN 1 ELSE 0 END),
            SUM(CASE WHEN rt.status = 0 THEN 1 ELSE 0 END)
        )
        FROM RoomTask rt
        WHERE MONTH(rt.assignedDate) = :month AND YEAR(rt.assignedDate) = :year
        GROUP BY rt.staff.id
    """)
    List<ThongkeTaskDto> getTaskStatsForAllStaffByMonth(@Param("month") int month, @Param("year") int year);

}
