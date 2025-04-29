package com.nguyenvanancodeweb.lakesidehotel.response;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDto {
    private Long id;

    private Long bookingId;

    private Long roomId;

    private Long userId;
    private String fullName;

    private double score;
    private String comment;
    private LocalDateTime createdAt;

    public RateDto(Rate rate) {
        this.id = rate.getId();
        this.bookingId = rate.getBookedRoom().getBookingId();
        this.roomId = rate.getRoom().getId();
        this.userId = rate.getUser().getId();
        this.fullName = rate.getUser().getFullName();
        this.score = rate.getScore();
        this.comment = rate.getComment();
        this.createdAt = rate.getCreatedAt();
    }
}
