package com.nguyenvanancodeweb.lakesidehotel.controller;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.request.RoomRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.room.AllRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.room.DetailRoomResponse;
import com.nguyenvanancodeweb.lakesidehotel.service.BookingService;
import com.nguyenvanancodeweb.lakesidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addNewRoom(
//            @RequestParam("photo") MultipartFile photo,
            @RequestBody RoomRequest roomRequest
    ) throws SQLException, IOException {
        try {
            Room savedRoom = roomService.addNewRoom(roomRequest.getName(), roomRequest.getDescription(),
                    roomRequest.getRoomType(), roomRequest.getRoomPrice(), roomRequest.getFloor(),
                    roomRequest.getMaxNumberAdult(), roomRequest.getMaxNumberChildren(),
                    roomRequest.getMaxNumberPeople(), roomRequest.getAgeLimit(), roomRequest.getNumberBed());
//        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
            return ResponseEntity.ok("Thêm thành công phòng!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<Page<AllRoomResponse>> getAllRooms(
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
        return ResponseEntity.ok(roomResponses);

    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok("Phòng được xóa thành công");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // thừa exception ResourceNotFoundException
    @PutMapping("/update/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest roomRequest)
            throws SQLException, IOException, ResourceNotFoundException {
//        byte[] photoBytes = photo != null && !photo.isEmpty() ?
//                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
//        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ?
//                new SerialBlob(photoBytes) : null;
        try {
            Room theRoom = roomService.updateRoom(roomId, roomRequest.getName(), roomRequest.getDescription(),
                    roomRequest.getRoomType(), roomRequest.getRoomPrice(), roomRequest.getFloor(),
                    roomRequest.getMaxNumberAdult(), roomRequest.getMaxNumberChildren(), roomRequest.getMaxNumberPeople(),
                    roomRequest.getAgeLimit(), roomRequest.getNumberBed());
//        theRoom.setPhoto1(photoBlob);
//        RoomResponse roomResponse = getRoomResponse(theRoom);
            return ResponseEntity.ok("Cập nhật thông tin phòng thành công");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<DetailRoomResponse>> getRoomById(@PathVariable Long roomId) throws ResourceNotFoundException {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            DetailRoomResponse detailRoomResponse = new DetailRoomResponse(room);
            return ResponseEntity.ok(Optional.of(detailRoomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<Page<AllRoomResponse>> getAvailableRooms(
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam() int numberAdult, @RequestParam() int numberChildren,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) throws SQLException {
        Page<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, numberAdult,
                numberChildren, pageNumber - 1, pageSize);
        Page<AllRoomResponse> allRoomRespons = availableRooms.map(AllRoomResponse::new);
//        for (Room room : availableRooms) {
//            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
//            if (photoBytes != null && photoBytes.length > 0) {
//                String photoBase64 = Base64.encodeBase64String(photoBytes);
//                AllRoomResponse allRoomResponse = getRoomResponse(room);
////                allRoomResponse.setPhoto1(photoBase64);
//                allRoomRespons.add(allRoomResponse);
//            }
//        }
        return ResponseEntity.ok(allRoomRespons);
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
