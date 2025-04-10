package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.request.RoomRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.ApiResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.DTO.DataResponseDTO;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.room.DetailRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.BookingService;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {
    // Tại sao không khai báo RoomService roomService mà khai báo như dưới, có hợp lệ?
    // Dependency Injection (DI) trong Spring: Khi khai báo như dưới, Sring sẽ tự động tìm kiếm và tiêm
    // 1 đối tượng cài đặt interface IRoomService vào trong RoomController.
    private final IRoomService roomService;

    private final BookingService bookingService;

    @PostMapping("/add/new-room")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> addNewRoom(
//            @RequestParam("photo") MultipartFile photo,
            @RequestBody RoomRequest roomRequest
    ) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(roomRequest.getName(), roomRequest.getDescription(),
                roomRequest.getRoomType(), roomRequest.getRoomPrice(), roomRequest.getFloor(),
                roomRequest.getMaxNumberAdult(), roomRequest.getMaxNumberChildren(),
                roomRequest.getMaxNumberPeople(), roomRequest.getAgeLimit(), roomRequest.getNumberBed(),
                roomRequest.getPhoto1(), roomRequest.getPhoto2(), roomRequest.getPhoto3(),
                roomRequest.getPhoto4(), roomRequest.getPhoto5());
//        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200"));
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<ApiResponseDTO<List<AllRoomResponse>>> getAllRooms(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize)
            throws SQLException, ResourceNotFoundException {
//        List<Room> rooms = roomService.getAllRooms();
//        List<AllRoomResponse> roomResponses = new ArrayList<>();
//        for (Room room : rooms) {
////            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
////            if(photoBytes != null && photoBytes.length > 0) {
////                String base64Photo = Base64.encodeBase64String(photoBytes);
////                RoomResponse roomResponse = getRoomResponse(room);
////                roomResponse.setPhoto1(base64Photo);
////                roomResponses.add(roomResponse);
////            }
//            roomResponses.add(new AllRoomResponse(room));
//        }
        Page<AllRoomResponse> roomResponses = roomService.getAllRooms(pageNumber - 1, pageSize);
        List<AllRoomResponse> rooms = roomResponses.getContent().stream().toList();

        DataResponseDTO<List<AllRoomResponse>> listDataResponseDTO = new DataResponseDTO<>(
                (int) roomResponses.getTotalElements(), rooms);
        ApiResponseDTO<List<AllRoomResponse>> apiResponseDTO = new ApiResponseDTO<>(true, "200",
                listDataResponseDTO);
        return ResponseEntity.ok(apiResponseDTO);
    }

    @DeleteMapping("/delete/room/{roomId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRoom(@PathVariable Long roomId) {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Phòng được xóa thành công"));
    }

    // thừa exception ResourceNotFoundException
    @PutMapping("/update/room/{roomId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDTO<Void>> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest roomRequest)
            throws SQLException, IOException, ResourceNotFoundException {
        try {
            Room theRoom = roomService.updateRoom(roomId, roomRequest.getName(), roomRequest.getDescription(),
                    roomRequest.getRoomType(), roomRequest.getRoomPrice(), roomRequest.getFloor(),
                    roomRequest.getMaxNumberAdult(), roomRequest.getMaxNumberChildren(),
                    roomRequest.getMaxNumberPeople(), roomRequest.getAgeLimit(), roomRequest.getNumberBed(),
                    roomRequest.getPhoto1(), roomRequest.getPhoto2(), roomRequest.getPhoto3(), roomRequest.getPhoto4(),
                    roomRequest.getPhoto5());
//        theRoom.setPhoto1(photoBlob);
//        RoomResponse roomResponse = getRoomResponse(theRoom);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Cập nhật phòng thành công"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, e.getMessage()));
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponseDTO<DetailRoomResponse>> getRoomById(@PathVariable Long roomId)
            throws ResourceNotFoundException {
        Room theRoom = roomService.getRoomById(roomId);
        DetailRoomResponse detailRoomResponse = new DetailRoomResponse(theRoom);

        DataResponseDTO<DetailRoomResponse> dataResponseDTO = new DataResponseDTO<>(null,
                detailRoomResponse);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<ApiResponseDTO<List<AllRoomResponse>>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam int numberAdult,
            @RequestParam int numberChildren,
            @RequestParam(required = false) String serviceIds,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean hasHighFloor,
            @RequestParam(required = false) Boolean hasHighRating,
            @RequestParam(required = false) Boolean hasTwoOrMoreBeds,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<Long> serviceIdList = new ArrayList<>();
        if (serviceIds != null && !serviceIds.isBlank()) {
            serviceIdList = Arrays.stream(serviceIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        Page<Room> availableRooms = roomService.getAvailableRooms(
                checkInDate, checkOutDate, numberAdult, numberChildren,
                serviceIdList, minPrice, maxPrice,
                hasHighFloor, hasHighRating, hasTwoOrMoreBeds,
                pageNumber - 1, pageSize
        );

        List<AllRoomResponse> responseList = availableRooms.getContent()
                .stream()
                .map(AllRoomResponse::new)
                .collect(Collectors.toList());

        DataResponseDTO<List<AllRoomResponse>> dataResponseDTO = new DataResponseDTO<>((int) availableRooms.getTotalElements(), responseList);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }

    @GetMapping("/available-day-in-month")
    public ResponseEntity<ApiResponseDTO<List<String>>> getAvailableDayInMonth(@RequestParam Long roomId,
                                                                                  @RequestParam int year,
                                                                                  @RequestParam int month){
        List<LocalDate> localDates = roomService.getAvailableDaysInMonth(roomId, year, month);
        List<String> formattedDays = localDates.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collect(Collectors.toList());
        DataResponseDTO<List<String>> dataResponseDTO = new DataResponseDTO<>(formattedDays.size(), formattedDays);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "200", dataResponseDTO));
    }

    // Tạo đối tượng roomResponse dùng nhiều lần mỗi khi thao tác với đối tượng này
    private AllRoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
//        List<BookingResponse> bookingInfo = bookings
//                .stream().
//                map(booking -> new BookingResponse(booking.getBookingId(),
//                        booking.getCheckInDate(),
//                        booking.getCheckOutDate(),
//                        booking.getBookingConfirmationCode())).toList();
//        byte[] photoBytes = null;
//        Blob photoBlob = room.getPhoto1();
//        if(photoBlob != null) {
//            try{
//                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
//            }catch (SQLException e){
//                throw new PhotoRetrievalException("Error retrieving photo");
//            }
//        }
        return new AllRoomResponse(room);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllRoomsByRoomId(roomId);
    }

}
