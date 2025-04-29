package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.request.RateRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.RateDto;
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
    public ResponseEntity<ApiResponseDTO<List<RateDto>>> getRateListByRoomId(@RequestParam int pageNumber,
                                                                             @RequestParam int pageSize,
                                                                             @RequestParam Long roomId) {
        Page<RateDto> rates = rateService.getRateListByRoomId(pageNumber, pageSize, roomId);
        DataResponseDTO<List<RateDto>> dataResponseDTO = new DataResponseDTO<>((int) rates.getTotalElements(), rates.getContent());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }



    @DeleteMapping("/delete-rate")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRate(@RequestParam Long rateId){
        rateService.deleteRate(rateId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Xoá phòng thành công"));
    }

}
