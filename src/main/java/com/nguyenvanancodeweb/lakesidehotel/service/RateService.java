package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Rate;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.repository.RateRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.RateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateService implements IRateService {
    private final RateRepository rateRepository;
    private final IRoomService roomService;
    private final IUserService userService;
    private final IBookingService bookingService;

    @Override
    public void addRate(RateRequest rateRequest) {
        Rate rate = new Rate();
        rate.setScore(rateRequest.getScore());
        rate.setComment(rateRequest.getComment());

        Room room = roomService.getRoomById(rateRequest.getRoomId());
        rate.setRoom(room);

        User user = userService.getUserByUserId(rateRequest.getUserid());
        rate.setUser(user);

        BookedRoom bookedRoom = bookingService.getBookedRoomById(rateRequest.getBookingId());
        rate.setBookedRoom(bookedRoom);

        rateRepository.save(rate);
    }

    @Override
    public Page<Rate> getRateListByRoomId(int pageNumber, int pageSize, Long roomId) {
        roomService.getRoomById(roomId); //check phong ton tai
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Rate> rates = rateRepository.findRateByRoomId(roomId, pageable);
        return rates;
    }

    @Override
    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
    }


}
