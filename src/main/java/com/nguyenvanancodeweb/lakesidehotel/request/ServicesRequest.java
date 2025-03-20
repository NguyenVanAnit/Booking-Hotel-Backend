package com.nguyenvanancodeweb.lakesidehotel.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServicesRequest {
    @NotNull(message = "Tên dịch vụ không được để trống")
    private String name;
    private String description;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private boolean isActive;

    @NotNull(message = "Giá dịch vụ không được để trống")
    private BigDecimal priceService;

    @NotNull(message = "Kiểu dịch vụ không được để trống")
    private String serviceType;

    @NotNull(message = "Trạng thái miễn phí không được để trống")
    private boolean isFree;

    @NotNull(message = "Số lượng tối đa không được để trống")
    private int maxQuantity;
}
