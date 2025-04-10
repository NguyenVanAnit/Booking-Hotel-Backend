package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.Services;
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
                    int numberBed, String photo1, String photo2, String photo3, String photo4,
                    String photo5) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    Page<AllRoomResponse> getAllRooms(int pageNumber, int pageSize);

    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException, ResourceNotFoundException;

    void deleteRoom(Long roomId);

    Room updateRoom(Long roomId, String name, String description, String roomType, BigDecimal roomPrice,
                    int floor, int maxNumberAdult, int maxNumberChildren, int maxNumberPeople, int ageLimit,
                    int numberBed, String photo1, String photo2, String photo3, String photo4,
                    String photo5 ) throws ResourceNotFoundException, SQLException;

    Room getRoomById(Long roomId);

    Page<Room> getAvailableRooms(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int numberAdult,
            int numberChildren,
            List<Long> serviceIds,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean hasHighFloor,
            Boolean hasHighRating,
            Boolean hasTwoOrMoreBeds,
            int pageNumber,
            int pageSize
    );

    Boolean checkAvailableRoom(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    List<LocalDate> getAvailableDaysInMonth(Long roomId, int year, int month);

    void validateRoomExists(Long roomId) throws ResourceNotFoundException;

    void addServiceToRoom(Long roomId, List<Services> services);

    void removeServiceFromRoom(Long roomId, Services services);
}
