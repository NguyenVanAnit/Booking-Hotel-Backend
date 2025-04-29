package com.nguyenvanancodeweb.lakesidehotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRevenueDto {
    private Long roomId;
    private String roomName;
    private BigDecimal revenue;
}
