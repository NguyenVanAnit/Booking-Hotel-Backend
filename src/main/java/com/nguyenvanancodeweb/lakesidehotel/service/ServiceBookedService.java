package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.repository.ServiceBookedRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.ServiceBookedRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceBookedService implements IServiceBookedService{
    private final ServiceBookedRepository serviceBookedRepository;

    @Override
    public void savingServiceForBooking(BookedRoom bookedRoom) {
//        List<Ser> serviceBookedRequests = bookedRoom.getServiceBookeds();

    }
}
