package com.nguyenvanancodeweb.lakesidehotel.exception;

import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleInvalidPagination(InvalidPaginationException e){
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException e){
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
    }
}
