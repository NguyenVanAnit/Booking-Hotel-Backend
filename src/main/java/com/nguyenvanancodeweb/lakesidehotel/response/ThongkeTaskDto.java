package com.nguyenvanancodeweb.lakesidehotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThongkeTaskDto {
    private Long staffId;
    private Long completedTaskCount;
    private Long pendingTaskCount;

    public ThongkeTaskDto(Long staffId, Long completedTaskCount, Long pendingTaskCount) {
        this.staffId = staffId;
        this.completedTaskCount = completedTaskCount;
        this.pendingTaskCount = pendingTaskCount;
    }
}
