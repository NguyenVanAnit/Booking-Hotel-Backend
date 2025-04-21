package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByRoomId(Long roomId);

    List<BookedRoom> findByGuestEmail(String email);

    @Query("SELECT b FROM BookedRoom b WHERE b.txnRef = :txnRef")
    BookedRoom findBookingByTxnRef(@Param("txnRef") String txnRef);
}
