package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query(" SELECT r FROM Room r " +
            " WHERE r.maxNumberAdult >= :numberAdult " +
            " AND r.maxNumberChildren >= :numberChildren " +
            " AND r.id NOT IN (" +
            "  SELECT br.room.id FROM BookedRoom br " +
            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")")
    Page<Room> findAvailableRoomByFilterStart(LocalDate checkInDate, LocalDate checkOutDate, int numberAdult,
                                              int numberChildren, Pageable pageable);

    @Query("SELECT b FROM BookedRoom b " +
            "WHERE b.room.id = :roomId " +
            "AND (b.checkInDate <= :endOfMonth AND b.checkOutDate >= :startOfMonth)")
    List<BookedRoom> findBookingsByRoomIdAndMonth(Long roomId, LocalDate startOfMonth, LocalDate endOfMonth);

}
