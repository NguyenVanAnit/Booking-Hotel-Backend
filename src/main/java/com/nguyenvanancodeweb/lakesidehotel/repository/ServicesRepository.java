package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    @Query("SELECT s FROM Services s " +
            "JOIN s.rooms r WHERE r.id = :roomId")
    List<Services> findServicesByRoomId(@Param("roomId") Long roomId);
}
