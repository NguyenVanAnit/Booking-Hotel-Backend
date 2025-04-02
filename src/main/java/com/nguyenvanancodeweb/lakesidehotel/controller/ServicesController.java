package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.ServicesResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.IServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServicesController {
    private final IServicesService servicesService;

    @PostMapping("/add-service")
    public ResponseEntity<ApiResponseDTO<Void>> addServices (@Valid @RequestBody ServicesRequest servicesRequest) {
        Services services = servicesService.addService(servicesRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Thêm dịch vụ thành công!"));
    }

    @GetMapping("/all-services")
    public ResponseEntity<ApiResponseDTO<List<ServicesResponse>>> getAllServices() {
        List<ServicesResponse> servicesResponses = servicesService.getAllServices();
        DataResponseDTO<List<ServicesResponse>> dataResponseDTO = new DataResponseDTO<>(servicesResponses.size(),
                servicesResponses);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }

    @PutMapping("/update-service/{serviceId}")
    public ResponseEntity<ApiResponseDTO<Void>> updateServices(@PathVariable Long serviceId ,
            @Valid @RequestBody ServicesRequest servicesRequest) {
        servicesService.updateService(serviceId, servicesRequest);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Cập nhật dịch vụ thành công!"));
    }

    @DeleteMapping("/delete-service/{serviceId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteService(@PathVariable Long serviceId) {
        servicesService.deleteService(serviceId);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Xoá phòng thành công!"));
    }

    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<ApiResponseDTO<List<ServicesResponse>>> getServicesByRoom(@PathVariable Long roomId) {
        List<ServicesResponse> servicesResponses = servicesService.getListOfServicesByRoomId(roomId);

        DataResponseDTO<List<ServicesResponse>> dataResponseDTO = new DataResponseDTO<>(servicesResponses.size(),
                servicesResponses);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }

    @PostMapping("/add-services/{roomId}")
    public ResponseEntity<ApiResponseDTO<Void>> addServiceToRoom(@PathVariable Long roomId,
                                                                 @Valid @RequestBody List<Long> serviceIds) {
        servicesService.addServicesListToRoom(roomId, serviceIds);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Thêm thành công dịch vụ vào phòng"));
    }

}
