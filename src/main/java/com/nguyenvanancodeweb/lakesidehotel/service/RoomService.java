package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.IllegalArgumentException;
import com.nguyenvanancodeweb.lakesidehotel.exception.InternalServerException;
import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidPaginationException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.repository.RoomRepository;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(String name, String description, String roomType, BigDecimal roomPrice, int floor,
                           int maxNumberAdult, int maxNumberChildren, int maxNumberPeople, int ageLimit,
                           int numberBed) throws SQLException, IOException {
        if (floor < 1) {
            throw new IllegalArgumentException("Số tầng không hợp lệ");
        }
        Room room = new Room();
        room.setName(name);
        room.setDescription(description);
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setFloor(floor);
        room.setMaxNumberAdult(maxNumberAdult);
        room.setMaxNumberChildren(maxNumberChildren);
        room.setMaxNumberPeople(maxNumberPeople);
        room.setAgeLimit(ageLimit);
        room.setNumberBed(numberBed);
//        if (!file.isEmpty()){
//            byte[] photoBytes = file.getBytes();
//            Blob photoBlob = new SerialBlob(photoBytes);
//            room.setPhoto1(photoBlob);
//        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Page<AllRoomResponse> getAllRooms(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize < 1) {
            throw new InvalidPaginationException("Số trang hoặc kích thước trang không hợp lệ!");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        return roomPage.map(AllRoomResponse::new);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }else{
            throw new ResourceNotFoundException("Phòng với ID " + roomId + " không được tìm thấy");
        }
    }

    @Override
    public Room updateRoom(Long roomId, String name, String description, String roomType, BigDecimal roomPrice,
                           int floor, int maxNumberAdult, int maxNumberChildren, int maxNumberPeople, int ageLimit,
                           int numberBed) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Phòng với ID " + roomId + " không được tìm thấy"));

        if (name != null) room.setName(name);
        if (description != null) room.setDescription(description);
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (floor >= 1) room.setFloor(floor);
        if (maxNumberAdult != 0) room.setMaxNumberAdult(maxNumberAdult);
        if (maxNumberChildren != 0) room.setMaxNumberChildren(maxNumberChildren);
        if (maxNumberPeople != 0) room.setMaxNumberPeople(maxNumberPeople);
        if (ageLimit != 0) room.setAgeLimit(ageLimit);
        if (numberBed != 0) room.setNumberBed(numberBed);
//        if (photoBytes != null && photoBytes.length > 0) {
//            try {
//                room.setPhoto1(new SerialBlob(photoBytes));
//            } catch (SQLException e) {
//                throw new InternalServerException("Error updating room");
//            }
//        }
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ID: " + roomId));
    }

    @Override
    public Page<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, int numberAdult,
                                        int numberChildren, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize < 1) {
            throw new InvalidPaginationException("Số trang hoặc kích thước trang không hợp lệ");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        return roomRepository.findAvailableRoomByFilterStart(checkInDate, checkOutDate, numberAdult,
                numberChildren, pageable);
    }

    @Override
    public Boolean checkAvailableRoom(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new ResourceNotFoundException("Phòng với ID " + roomId + " không được tìm thấy"));
//        List<BookedRoom> existingBookings = room.getBookings();

        return true;
    }

    @Override
    public List<LocalDate> getAvailableDaysInMonth(Long roomId, int year, int month) {
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Không tồn tại tháng " + month);
        }
        if(year < 1900) {
            throw new IllegalArgumentException("Không có dữ liệu về năm " + year);
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<BookedRoom> bookedRoomsInMonth = roomRepository.findBookingsByRoomIdAndMonth(roomId, startOfMonth,
                endOfMonth);
        List<LocalDate> bookedDays = bookedRoomsInMonth.stream()
                .flatMap(booking -> booking.getCheckInDate().datesUntil(booking.getCheckOutDate().plusDays(1)))
                .collect(Collectors.toList());
        List<LocalDate> allDaysInMonth = IntStream.rangeClosed(1, yearMonth.lengthOfMonth())
                .mapToObj(day -> LocalDate.of(year, month, day))
                .collect(Collectors.toList());
        return allDaysInMonth.stream()
                .filter(day -> !bookedDays.contains(day))
                .collect(Collectors.toList());
    }

    @Override
    public void validateRoomExists(Long roomId) throws ResourceNotFoundException {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Không tồn tại phòng ID " + roomId);
        };
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException, ResourceNotFoundException {
        //Optional: cách an toàn để xử lý dữ liệu có thể null
        // Nếu null trả về Optional.empty(), nếu k null thì trả về đối tượng Room
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room Not Found");
        }
        // phải .get() rồi mới .getPhoto() vì sử dụng đối tượng Optional,
        // theRoom đang là đối tượng Optional, theRoom.get() là đối tượng Room
        Blob photoBlob = theRoom.get().getPhoto1();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

}
