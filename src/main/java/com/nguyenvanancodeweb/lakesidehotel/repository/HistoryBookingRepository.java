package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryBookingRepository extends JpaRepository<HistoryBooking, Long> {
    @Query("SELECT h FROM HistoryBooking h WHERE h.user.id = :userId")
    Page<HistoryBooking> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT h FROM HistoryBooking h WHERE h.user.id = :roomId")
    Page<HistoryBooking> findByRoomId(Long roomId, Pageable pageable);

    Page<HistoryBooking> findAll(Pageable pageable);
}
