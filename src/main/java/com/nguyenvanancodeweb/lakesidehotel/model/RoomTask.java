package com.nguyenvanancodeweb.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Staff staff;

    private Integer taskType; // 0 - Dọn dẹp hàng ngày | 1 - Bảo trì | 2 - Kiểm tra | 3 - Dọn dẹp sau checkout
    private Integer status; // 0 - Chưa làm | 1 - Hoàn thành | 2 - Bị huỷ

    private LocalDate assignedDate;
    private LocalDate completedDate;

    private String notes;
}
