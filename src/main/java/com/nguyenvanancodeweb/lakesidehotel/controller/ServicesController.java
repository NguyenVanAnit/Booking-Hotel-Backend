package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
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
    public ResponseEntity<String> addServices (@Valid @RequestBody ServicesRequest servicesRequest) {
        Services services = servicesService.addService(servicesRequest);
        return ResponseEntity.ok("Thêm dịch vụ thành công!");
    }

    @GetMapping("/all-services")
    public ResponseEntity<Page<ServicesResponse>> getAllServices(@RequestParam int pageNumber,
                                                                 @RequestParam int pageSize) {
        Page<ServicesResponse> servicesResponses = servicesService.getAllServices(pageNumber - 1, pageSize);
        return ResponseEntity.ok(servicesResponses);
    }

    @PutMapping("/update-service/{serviceId}")
    public ResponseEntity<String> updateServices(@PathVariable Long serviceId ,
            @Valid @RequestBody ServicesRequest servicesRequest) {
        servicesService.updateService(serviceId, servicesRequest);
        return ResponseEntity.ok("Cập nhật dịch vụ thành công!");
    }

    @DeleteMapping("/delete-service/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable Long serviceId) {
        servicesService.deleteService(serviceId);
        return ResponseEntity.ok("Xoá phòng thành công!");
    }
}
