package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.HistoryBooking;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.HistoryBookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.IHistoryBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history-booking")
public class HistoryBookingController {
    private final IHistoryBookingService historyBookingService;

    @GetMapping("/all-history")
    public ResponseEntity<ApiResponseDTO<List<HistoryBookingResponse>>> getAllHistoryBookings(
            @RequestParam int pageNumber, @RequestParam int pageSize,
                                                                                              @RequestParam Long userId,
                                                                                              @RequestParam Long roomId) {
        Page<HistoryBooking> historyBookings = historyBookingService.getHistoryBooking(pageNumber - 1,
                pageSize, userId, roomId);

        List<HistoryBookingResponse> historyBookingResponses = historyBookings.getContent().stream()
                .map(HistoryBookingResponse::new)
                .toList();

        DataResponseDTO<List<HistoryBookingResponse>> dataResponseDTO = new DataResponseDTO<>(
                (int) historyBookings.getTotalElements(), historyBookingResponses);
        ApiResponseDTO<List<HistoryBookingResponse>> apiResponseDTO = new ApiResponseDTO<>(true, "200",
                dataResponseDTO);
        return ResponseEntity.ok(apiResponseDTO) ;
    }

}
