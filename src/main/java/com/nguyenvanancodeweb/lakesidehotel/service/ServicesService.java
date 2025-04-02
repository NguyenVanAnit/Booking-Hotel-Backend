package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidPaginationException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.repository.ServicesRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.ServicesRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.ServicesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesService implements IServicesService {
    private final ServicesRepository servicesRepository;
    private final IRoomService roomService;

    public Services addService(ServicesRequest servicesRequest) {
        if (servicesRequest.getPriceService().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Phí dịch vụ không hợp lệ");
        }
        if (servicesRequest.getMaxQuantity() < 0) {
            throw new IllegalArgumentException("Giới hạn số lượng dịch vụ không hợp lệ");
        }
        Services service = new Services();
        return saveServices(servicesRequest, service);
    }

    @Override
    public List<ServicesResponse> getAllServices() {
        List<Services> servicesPage = servicesRepository.findAll();
        return servicesPage.stream().map(ServicesResponse::new).toList();
    }

    @Override
    public Services updateService(Long id, ServicesRequest serviceRequest) {
        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không có phòng với ID: " + id));

        if (serviceRequest.getPriceService().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Phí dịch vụ không hợp lệ");
        }
        if (serviceRequest.getMaxQuantity() < 0) {
            throw new IllegalArgumentException("Giới hạn số lượng dịch vụ không hợp lệ");
        }
        return saveServices(serviceRequest, services);
    }

    @Override
    public void deleteService(Long id) {
        Services services = servicesRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Không có phòng với ID: " + id));
        servicesRepository.deleteById(id);
    }

    @Override
    public List<ServicesResponse> getListOfServicesByRoomId(Long roomId) {
        roomService.validateRoomExists(roomId); //check ton tai
        List<Services> services = servicesRepository.findServicesByRoomId(roomId);
        return services.stream().map(ServicesResponse::new).toList();
    }

    @Override
    public Services getServicesById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dịch vụ phòng"));
    }

    @Override
    public void addServicesListToRoom(Long roomId, List<Long> servicesIds) {
        List<Services> services = servicesRepository.findAllById(servicesIds);
        roomService.addServiceToRoom(roomId, services);
    }


    //Dùng để tránh lặp code giữa phần add và update
    private Services saveServices(ServicesRequest servicesRequest, Services service) {
        service.setName(servicesRequest.getName());
        service.setDescription(servicesRequest.getDescription());
        service.setActive(servicesRequest.isActive());
        service.setPriceService(servicesRequest.getPriceService());
        service.setServiceType(servicesRequest.getServiceType());
        service.setFree(servicesRequest.isFree());
        service.setMaxQuantity(servicesRequest.getMaxQuantity());

        return servicesRepository.save(service);
    }
}
