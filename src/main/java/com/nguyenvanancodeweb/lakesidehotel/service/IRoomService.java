package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Room addNewRoom(String name, String description, String roomType, BigDecimal roomPrice, int floor,
                    int maxNumberAdult, int maxNumberChildren, int maxNumberPeople, int ageLimit,
                    int numberBed) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    Page<AllRoomResponse> getAllRooms(int pageNumber, int pageSize);

    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException, ResourceNotFoundException;

    void deleteRoom(Long roomId);

    Room updateRoom(Long roomId, String name, String description, String roomType, BigDecimal roomPrice,
                    int floor, int maxNumberAdult, int maxNumberChildren, int maxNumberPeople, int ageLimit,
                    int numberBed ) throws ResourceNotFoundException;

    Optional<Room> getRoomById(Long roomId);

    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int numberAdult, int numberChildren,
                                 int pageNumber, int pageSize);
}
