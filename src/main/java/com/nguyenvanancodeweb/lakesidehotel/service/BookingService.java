package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.model.User;
import com.nguyenvanancodeweb.lakesidehotel.repository.BookingRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.PaymentConfirmationRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.ServiceBookedRequest;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingDetailDto;
import com.nguyenvanancodeweb.lakesidehotel.response.BookingResponse;
import com.nguyenvanancodeweb.lakesidehotel.response.RoomRevenueDto;
import com.nguyenvanancodeweb.lakesidehotel.response.ServiceBookedDto;
import com.nguyenvanancodeweb.lakesidehotel.utils.VNPAYConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final IHistoryBookingService historyBookingService;
    private final IServiceBookedService serviceBookedService;
    private final IUserService userService;

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public List<BookedRoom> getAllRoomsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public BookedRoom saveBooking(Long roomId, BookingRequest bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Ngày nhận phòng phải trước ngày trả phòng");
        }
        Room room = roomService.getRoomById(roomId);
        List<BookedRoom> existingBooking = room.getBookings();
        Boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBooking);
        if (roomIsAvailable) {
//            bookingRequest.setStatus(0);
            BookedRoom bookedRoom = new BookedRoom();
            bookedRoom.setCheckInDate(bookingRequest.getCheckInDate());
            bookedRoom.setCheckOutDate(bookingRequest.getCheckOutDate());
            bookedRoom.setGuestEmail(bookingRequest.getGuestEmail());
            bookedRoom.setNumOfAdults(bookingRequest.getNumOfAdults());
            bookedRoom.setNumOfChildren(bookingRequest.getNumOfChildren());
            bookedRoom.setPhoneNumber(bookingRequest.getPhoneNumberOther());
            bookedRoom.setUserId(bookingRequest.getUserId());
            bookedRoom.setRoom(room);
            bookedRoom.setStatus(0);
            bookedRoom.setBookingTime(String.valueOf(LocalDate.now()));
            bookedRoom.setIsChecked(0);

            //tinh so ngay tu checkin den checkout
            long daysStayed = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

            // Tính giá tiền thuê phòng
            BigDecimal roomPricePerDay = room.getRoomPrice(); // Giả sử Room có phương thức getPrice()
            BigDecimal totalRoomPrice = roomPricePerDay.multiply(BigDecimal.valueOf(daysStayed));

            List<ServiceBookedRequest> serviceBookedRequests = bookingRequest.getServiceBookedRequests();
            bookingRepository.save(bookedRoom);
            BigDecimal totalPriceService = serviceBookedService.savingServiceForBooking(bookedRoom, serviceBookedRequests);

            //luu gia tri tong tien vua tinh vao don dat phong
            bookedRoom.setTotalPrice(totalRoomPrice.add(totalPriceService));

            bookingRepository.save(bookedRoom);
            room.addBooking(bookedRoom);
            historyBookingService.addHistoryBooking(bookedRoom);

            return bookedRoom;
        }else {
            throw new InvalidBookingRequestException("Xin lỗi, căn phòng không trống vào ngày được chọn");
        }
    }

    @Override
    public void saveBookingByLetan(Long roomId, BookingRequest bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Ngày nhận phòng phải trước ngày trả phòng");
        }
        Room room = roomService.getRoomById(roomId);
        List<BookedRoom> existingBooking = room.getBookings();
        Boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBooking);
        if (roomIsAvailable) {
//            bookingRequest.setStatus(0);
            BookedRoom bookedRoom = new BookedRoom();
            bookedRoom.setCheckInDate(bookingRequest.getCheckInDate());
            bookedRoom.setCheckOutDate(bookingRequest.getCheckOutDate());
            bookedRoom.setGuestEmail(bookingRequest.getGuestEmail());
            bookedRoom.setNumOfAdults(bookingRequest.getNumOfAdults());
            bookedRoom.setNumOfChildren(bookingRequest.getNumOfChildren());
            bookedRoom.setPhoneNumber(bookingRequest.getPhoneNumberOther());
            bookedRoom.setUserId(bookingRequest.getUserId());
            bookedRoom.setRoom(room);
            bookedRoom.setStatus(1);
            bookedRoom.setBookingTime(String.valueOf(LocalDate.now()));
            bookedRoom.setIsChecked(0);
            bookedRoom.setTxnRef("TrucTiep");

            //tinh so ngay tu checkin den checkout
            long daysStayed = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

            // Tính giá tiền thuê phòng
            BigDecimal roomPricePerDay = room.getRoomPrice(); // Giả sử Room có phương thức getPrice()
            BigDecimal totalRoomPrice = roomPricePerDay.multiply(BigDecimal.valueOf(daysStayed));

            List<ServiceBookedRequest> serviceBookedRequests = bookingRequest.getServiceBookedRequests();
            bookingRepository.save(bookedRoom);
            BigDecimal totalPriceService = serviceBookedService.savingServiceForBooking(bookedRoom, serviceBookedRequests);

            //luu gia tri tong tien vua tinh vao don dat phong
            bookedRoom.setTotalPrice(totalRoomPrice.add(totalPriceService));

            bookingRepository.save(bookedRoom);
            room.addBooking(bookedRoom);
            historyBookingService.addHistoryBooking(bookedRoom);
        }else {
            throw new InvalidBookingRequestException("Xin lỗi, căn phòng không trống vào ngày được chọn");
        }
    }

    @Override
    public void updateBookingStatus(Long bookingId, int status) {
        Optional<BookedRoom> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            BookedRoom booking = bookingOpt.get();
            booking.setStatus(status);
            bookingRepository.save(booking);
        } else {
            throw new InvalidBookingRequestException("Không tìm thấy đặt phòng với ID: " + bookingId);
        }
    }

    @Override
    public void saveTxnRef(String TxnRef, Long bookingId) {
        BookedRoom bookedRoom = getBookedRoomById(bookingId);
        bookedRoom.setTxnRef(TxnRef);
        bookingRepository.save(bookedRoom);
    }

    @Override
    public BookingResponse confirmPayment(PaymentConfirmationRequest confirmationRequest) {
        // Lấy các thông tin từ request
        String vnp_TxnRef = confirmationRequest.getVnp_TxnRef();
        String vnp_ResponseCode = confirmationRequest.getVnp_ResponseCode();
//            String vnp_SecureHash = confirmationRequest.getVnp_SecureHash();
        BookedRoom bookedRoom = getBookingByTxnRef(vnp_TxnRef);
        BookingResponse bookingResponse = mapToResponse(bookedRoom);
        // Kiểm tra ResponseCode (nếu là "00" thì là thành công)
        if (!"00".equals(vnp_ResponseCode)) {
            bookedRoom.setStatus(2);
            bookingResponse.setStatus(2);
            bookingRepository.save(bookedRoom);
            historyBookingService.updateStatusHistoryBooking(bookedRoom.getBookingId());
            return bookingResponse;
        }
        bookedRoom.setStatus(1);
        bookingResponse.setStatus(1);
        bookingRepository.save(bookedRoom);
        historyBookingService.updateStatusHistoryBooking(bookedRoom.getBookingId());
        return bookingResponse;
    }

    @Override
    public BookedRoom getBookingByTxnRef(String txnRef) {
        BookedRoom bookedRoom = bookingRepository.findBookingByTxnRef(txnRef);
        return bookedRoom;
    }

    @Override
    public Long getMonthlyBookingCount(Long roomId, int month, int year) {
        return bookingRepository.countBookingsByMonth(roomId, month, year);
    }

    @Override
    public Long getYearlyBookingCount(Long roomId, int year) {
        return bookingRepository.countBookingsByYear(roomId, year);
    }

    public List<RoomRevenueDto> getRoomRevenue(Integer month, Integer year) {
        return bookingRepository.findRoomRevenueByMonthAndYear(month, year);
    }

    @Override
    public List<BookingResponse> getBookingsByCheckInDate(LocalDate checkInDate) {
        List<BookedRoom> bookings = bookingRepository.findByCheckInDate(checkInDate);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateChecked(Long bookingId, int status) {
        BookedRoom bookedRoom = getBookedRoomById(bookingId);
        bookedRoom.setIsChecked(status);
        historyBookingService.updateIsCheckedHistoryBooking(bookingId, status);
        bookingRepository.save(bookedRoom);
    }

    @Override
    public BookingDetailDto getDetailBooking(Long bookingId) {
        BookedRoom bookedRoom = getBookedRoomById(bookingId);

        BookingDetailDto bookingDetailDto = new BookingDetailDto();
        bookingDetailDto.setBookingId(bookedRoom.getBookingId());
        bookingDetailDto.setCheckInDate(bookedRoom.getCheckInDate());
        bookingDetailDto.setCheckOutDate(bookedRoom.getCheckOutDate());
        bookingDetailDto.setGuestEmail(bookedRoom.getGuestEmail());
        bookingDetailDto.setNumOfAdults(bookedRoom.getNumOfAdults());
        bookingDetailDto.setNumOfChildren(bookedRoom.getNumOfChildren());
        bookingDetailDto.setBookingConfirmationCode(bookedRoom.getBookingConfirmationCode());
        bookingDetailDto.setBookingTime(bookedRoom.getBookingTime());
        bookingDetailDto.setStatus(bookedRoom.getStatus());
        bookingDetailDto.setPhoneNumber(bookedRoom.getPhoneNumber());
        bookingDetailDto.setTransactionCode(bookedRoom.getTransactionCode());
        bookingDetailDto.setBank(bookedRoom.getBank());
        bookingDetailDto.setTotalPrice(bookedRoom.getTotalPrice());
        bookingDetailDto.setRoomId(bookedRoom.getRoom() != null ? bookedRoom.getRoom().getId() : null);
        bookingDetailDto.setUserId(bookedRoom.getUserId());
        bookingDetailDto.setTxnRef(bookedRoom.getTxnRef());
        bookingDetailDto.setIsChecked(bookedRoom.getIsChecked());

        User user = userService.getUserByUserId(bookedRoom.getUserId());
        bookingDetailDto.setEmail(user.getEmail());
        bookingDetailDto.setPhoneNumberUser(user.getPhoneNumber());
        bookingDetailDto.setFullName(user.getFullName());

        // Map List<ServiceBooked> -> List<ServiceBookedDto>
        if (bookedRoom.getServiceBookeds() != null && !bookedRoom.getServiceBookeds().isEmpty()) {
            List<ServiceBookedDto> serviceDtos = bookedRoom.getServiceBookeds().stream()
                    .map(serviceBooked -> {
                        ServiceBookedDto dto = new ServiceBookedDto();
                        dto.setId(serviceBooked.getId());
                        dto.setQuantity(serviceBooked.getQuantity());
                        dto.setTotalPrice(serviceBooked.getTotalPrice());
                        dto.setServiceName(serviceBooked.getServices() != null ? serviceBooked.getServices().getName() : null);
                        return dto;
                    })
                    .collect(Collectors.toList());

            bookingDetailDto.setServiceBookedDtos(serviceDtos);
        } else {
            bookingDetailDto.setServiceBookedDtos(Collections.emptyList());
        }

        return bookingDetailDto;
    }

    @Override
    public Long getBookingIdByConfirmationCode(String confirmationCode) {
        return bookingRepository.findBookingIdByConfirmationCode(confirmationCode);
    }


//            // Đảm bảo checksum chính xác để bảo mật (làm lại secure hash)
//            String params = buildParamsForHash(confirmationRequest);  // Dựng lại chuỗi tham số
//            String expectedSecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.vnp_HashSecret, params);
//            if (!vnp_SecureHash.equals(expectedSecureHash)) {
//                return "Dữ liệu không hợp lệ";
//            }
//
//            // Lấy thông tin đặt phòng từ database
//            BookedRoom booking = bookingService.findBookingByTxnRef(vnp_TxnRef);
//            if (booking == null) {
//                return ResponseEntity.badRequest().body(new ApiResponseDTO<>(false, "Không tìm thấy đơn đặt phòng"));
//            }
//
//            // Cập nhật trạng thái của đơn đặt phòng
//            booking.setStatus("CONFIRMED"); // Hoặc bất kỳ trạng thái nào cho thành công
//            bookingService.saveBooking(booking); // Lưu thay đổi
//
//            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Thanh toán thành công", null));
//    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public void acceptBooking(Long bookingId) {
        BookedRoom booking = bookingRepository.findById(bookingId).get();
        booking.setStatus(1);
        bookingRepository.save(booking);

        // Lấy tất cả các đơn đặt phòng cùng phòng
        List<BookedRoom> roomBookings = bookingRepository.findByRoomId(booking.getRoom().getId());

        // Hủy các đơn chưa được duyệt (status == 0) và trùng ngày
        roomBookings.stream()
                .filter(existingBooking -> existingBooking.getStatus() == 0)
                .filter(existingBooking ->
                        existingBooking.getCheckInDate().isBefore(booking.getCheckOutDate()) &&
                                existingBooking.getCheckOutDate().isAfter(booking.getCheckInDate()))
                .forEach(existingBooking -> {
                    existingBooking.setStatus(2);
                    bookingRepository.save(existingBooking);
                });
    }

    @Override
    public void rejectBooking(Long bookingId) {
        BookedRoom booking = bookingRepository.findById(bookingId).get();
        booking.setStatus(4);
        bookingRepository.save(booking);
    }

    @Override
    public BookedRoom getBookedRoomById(Long bookingId) {
        BookedRoom bookedRoom = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID " + bookingId));
        return bookedRoom;
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookingResponse> getAllBookingResponses() {
        List<BookedRoom> bookings = getAllBookings();
        List<BookingResponse> responses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            responses.add(mapToResponse(booking));
        }
        return responses;
    }

    private BookingResponse mapToResponse(BookedRoom booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setStatus(booking.getStatus());
        response.setTotalPrice(booking.getTotalPrice());
        response.setUserId(booking.getUserId());
        response.setRoomId(booking.getRoom().getId()); // nhớ room không null
        response.setNumOfAdults(booking.getNumOfAdults());
        response.setNumOfChildren(booking.getNumOfChildren());
        response.setBookingTime(booking.getBookingTime());
        response.setRoomName(booking.getRoom().getName());
        response.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        response.setIsChecked(booking.getIsChecked());

        User user = userService.getUserByUserId(booking.getUserId());
        response.setGuestName(user.getFullName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        return response;
    }

    private Boolean roomIsAvailable(BookingRequest bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .filter(existingBooking -> existingBooking.getStatus() == 1)
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

}
