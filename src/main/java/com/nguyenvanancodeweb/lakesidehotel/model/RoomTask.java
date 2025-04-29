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

    private Integer taskType; // 0 - Dọn dẹp | 1 - Bảo trì | 2 - Kiểm tra
    private Integer status; // 0 - Chưa làm | 1 - Đang làm | 2 - Hoàn thành | 3 - Bị huỷ

    private LocalDate assignedDate;
    private LocalDate completedDate;

    private String notes;
}
