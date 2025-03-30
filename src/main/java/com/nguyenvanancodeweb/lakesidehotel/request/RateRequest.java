package com.nguyenvanancodeweb.lakesidehotel.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RateRequest {
    @NotNull(message = "Điểm đánh giá không được để trống")
    private double score;
    private String comment;

    @NotNull(message = "ID đơn đặt phòng không được để trống")
    private Long bookingId;

    @NotNull(message = "ID phòng không được để trống")
    private Long roomId;

    @NotNull(message = "ID người dùng không được để trống")
    private Long userid;
}
