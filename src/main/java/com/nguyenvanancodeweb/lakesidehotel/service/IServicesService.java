package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.ServicesResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IServicesService {
    Services addService(ServicesRequest servicesRequest);

    List<ServicesResponse> getAllServices();

    Services updateService(Long id, ServicesRequest servicesRequest);

    void deleteService(Long id);

    List<ServicesResponse> getListOfServicesByRoomId(Long roomId);

    Services getServicesById(Long id);
}
