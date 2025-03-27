package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.request.RateRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rate")
public class RateController {
    private final RateService rateService;

    @PostMapping("/add-rate")
    public ResponseEntity<ApiResponseDTO<Void>> addRate(@RequestBody RateRequest rateRequest){
        rateService.addRate(rateRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Đánh giá thành công!"));
    }

    @GetMapping("/get-by-room-id")
    public ResponseEntity<ApiResponseDTO<List<Rate>>> getRateListByRoomId(@RequestParam int pageNumber,
                                                                          @RequestParam int pageSize,
                                                                          @RequestParam Long roomId){
        Page<Rate> rates = rateService.getRateListByRoomId(pageNumber, pageSize, roomId);
        List<Rate> rateList = rates.getContent().stream().toList();

        DataResponseDTO<List<Rate>> dataResponseDTO = new DataResponseDTO<>((int) rates.getTotalElements(), rateList);
        return ResponseEntity.ok(new ApiResponseDTO<List<Rate>>(true, "200", dataResponseDTO));
    }

}
