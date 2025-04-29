package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.response.RoomRevenueDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByRoomId(Long roomId);

    List<BookedRoom> findByGuestEmail(String email);

    @Query("SELECT b FROM BookedRoom b WHERE b.txnRef = :txnRef")
    BookedRoom findBookingByTxnRef(@Param("txnRef") String txnRef);

    @Query("SELECT COUNT(br) FROM BookedRoom br " +
            "WHERE br.room.id = :roomId " +
            "AND FUNCTION('MONTH', br.checkInDate) = :month " +
            "AND FUNCTION('YEAR', br.checkInDate) = :year")
    Long countBookingsByMonth(
            @Param("roomId") Long roomId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT COUNT(br) FROM BookedRoom br " +
            "WHERE br.room.id = :roomId " +
            "AND FUNCTION('YEAR', br.checkInDate) = :year")
    Long countBookingsByYear(
            @Param("roomId") Long roomId,
            @Param("year") int year
    );

    @Query("""
    SELECT new com.nguyenvanancodeweb.lakesidehotel.response.RoomRevenueDto(
        r.id, r.name, SUM(b.totalPrice)
    )
    FROM BookedRoom b
    JOIN b.room r
    WHERE (:month IS NULL OR FUNCTION('MONTH', b.checkInDate) = :month)
      AND (:year IS NULL OR FUNCTION('YEAR', b.checkInDate) = :year)
      AND b.status = 1
    GROUP BY r.id, r.name
    ORDER BY SUM(b.totalPrice) DESC
""")
    List<RoomRevenueDto> findRoomRevenueByMonthAndYear(
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    List<BookedRoom> findByCheckInDate(LocalDate checkInDate);

    @Query("SELECT b.bookingId FROM BookedRoom b WHERE b.bookingConfirmationCode = :code")
    Long findBookingIdByConfirmationCode(@Param("code") String code);

}
