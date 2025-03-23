package com.nguyenvanancodeweb.lakesidehotel.response.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean isSuccess;
    private String message;
    private DataResponseDTO<T> data;

    public ApiResponseDTO(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = null;
    }
}
