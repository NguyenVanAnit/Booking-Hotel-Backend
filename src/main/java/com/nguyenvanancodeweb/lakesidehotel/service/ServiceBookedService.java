package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.ServiceBooked;
import com.nguyenvanancodeweb.lakesidehotel.model.Services;
import com.nguyenvanancodeweb.lakesidehotel.repository.ServiceBookedRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.ServiceBookedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceBookedService implements IServiceBookedService{
    private final ServiceBookedRepository serviceBookedRepository;
    private final IServicesService servicesService;

    @Override
    public BigDecimal savingServiceForBooking(BookedRoom bookedRoom, List<ServiceBookedRequest> serviceBookedRequests) {
        List<ServiceBooked> serviceBookeds = serviceBookedRequests.stream().map(serviceBookedRequest -> {
            ServiceBooked serviceBooked = new ServiceBooked();
            serviceBooked.setBookedRoom(bookedRoom);
            serviceBooked.setQuantity(serviceBookedRequest.getQuantity());

            Services services = servicesService.getServicesById(serviceBookedRequest.getServiceId());

            serviceBooked.setTotalPrice(services.getPriceService().multiply(BigDecimal.valueOf(serviceBooked.getQuantity())));
            serviceBooked.setServices(services);

            return serviceBooked;
        }).collect(Collectors.toList());

        BigDecimal totalPriceService = serviceBookeds.stream()
                .map(ServiceBooked::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        serviceBookedRepository.saveAll(serviceBookeds);
        return totalPriceService;
    }
}
