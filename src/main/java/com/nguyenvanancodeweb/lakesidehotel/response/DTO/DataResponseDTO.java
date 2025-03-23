package com.nguyenvanancodeweb.lakesidehotel.response.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DataResponseDTO<T> {
    private Integer totalRecords;
    private T data;
}
