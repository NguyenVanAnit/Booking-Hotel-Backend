package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.request.ServiceBookedRequest;

import java.math.BigDecimal;
import java.util.List;

public interface IServiceBookedService {
    BigDecimal savingServiceForBooking(BookedRoom bookedRoom, List<ServiceBookedRequest> serviceBookedRequests);
}
