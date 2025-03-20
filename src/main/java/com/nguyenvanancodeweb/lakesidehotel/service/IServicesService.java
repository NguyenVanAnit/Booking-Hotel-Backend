package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.ServicesResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface IServicesService {
    Services addService(ServicesRequest servicesRequest);

    Page<ServicesResponse> getAllServices(int pageNumber, int pageSize);

    Services updateService(Long id, ServicesRequest servicesRequest);

    void deleteService(Long id);
}
