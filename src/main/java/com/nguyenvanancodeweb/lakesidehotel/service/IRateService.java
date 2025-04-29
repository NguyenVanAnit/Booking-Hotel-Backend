package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.request.RateRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.RateDto;
import org.springframework.data.domain.Page;

public interface IRateService {
    void addRate(RateRequest rateRequest);

    Page<RateDto> getRateListByRoomId(int pageNumber, int pageSize, Long roomId);

    void deleteRate(Long id);
}
