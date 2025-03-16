package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ServicesService implements IServicesService {
    private final ServicesRepository servicesRepository;

    public Services addService(String name, String description, String isActive, BigDecimal priceService,
                               String serviceType, boolean isFree, int maxQuantity) {
        if (priceService.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Phí dịch vụ không hợp lệ");
        }
        if (maxQuantity < 0) {
            throw new IllegalArgumentException("Giới hạn số lượng dịch vụ không hợp lệ");
        }
        Services service = new Services();
        service.setName(name);
        service.setDescription(description);
        service.setPriceService(priceService);
        service.setServiceType(serviceType);
        service.setFree(isFree);
        service.setMaxQuantity(maxQuantity);

        return servicesRepository.save(service);
    }
}
