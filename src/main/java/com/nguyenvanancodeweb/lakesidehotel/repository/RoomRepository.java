package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("""
    SELECT r FROM Room r
    WHERE r.maxNumberAdult >= :numberAdult
      AND r.maxNumberChildren >= :numberChildren
      AND r.maxNumberPeople >= (:numberAdult + :numberChildren)
      AND (:minPrice IS NULL OR r.roomPrice >= :minPrice)
      AND (:maxPrice IS NULL OR r.roomPrice <= :maxPrice)
      AND (:hasHighFloor = false OR r.floor > 15)
      AND (:hasHighRating = false OR r.totalRating > 4)
      AND (:hasTwoOrMoreBeds = false OR r.numberBed >= 2)
      AND (
       :checkInDate IS NULL OR :checkOutDate IS NULL
       OR r.id NOT IN (
           SELECT br.room.id FROM BookedRoom br
           WHERE br.checkInDate < :checkOutDate AND br.checkOutDate > :checkInDate
       )
     )
      AND (
        :#{#serviceIds == null || #serviceIds.isEmpty()} = true
        OR EXISTS (
            SELECT 1 FROM Room r2
            JOIN r2.services s
            WHERE r2.id = r.id
            GROUP BY r2.id
            HAVING COUNT(DISTINCT s.id) >= :#{#serviceIds.size()}
               AND SUM(CASE WHEN s.id IN :serviceIds THEN 1 ELSE 0 END) = :#{#serviceIds.size()}
        )
      )
""")
    Page<Room> findAvailableRooms(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("numberAdult") int numberAdult,
            @Param("numberChildren") int numberChildren,
            @Param("serviceIds") List<Long> serviceIds,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("hasHighFloor") boolean hasHighFloor,
            @Param("hasHighRating") boolean hasHighRating,
            @Param("hasTwoOrMoreBeds") boolean hasTwoOrMoreBeds,
            Pageable pageable
    );


}
