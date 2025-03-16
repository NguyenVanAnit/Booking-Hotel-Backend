package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
import com.nguyenvanancodeweb.lakesidehotel.service.IServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServicesController {
    private final IServicesService servicesService;

    @PostMapping("/add-service")
    public ResponseEntity<String> addServices (@RequestBody ServicesRequest servicesRequest) {

        return ResponseEntity.ok("Thêm dịch vụ thành công!");
    }
}
