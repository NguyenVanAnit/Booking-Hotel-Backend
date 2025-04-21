package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.InvalidBookingRequestException;
import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.BookedRoom;
import com.nguyenvanancodeweb.lakesidehotel.model.Room;
import com.nguyenvanancodeweb.lakesidehotel.repository.BookingRepository;
import com.nguyenvanancodeweb.lakesidehotel.request.BookingRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.PaymentConfirmationRequest;
import com.nguyenvanancodeweb.lakesidehotel.request.ServiceBookedRequest;
import com.nguyenvanancodeweb.lakesidehotel.utils.VNPAYConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final IHistoryBookingService historyBookingService;
    private final IServiceBookedService serviceBookedService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

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
    public Boolean confirmPayment(PaymentConfirmationRequest confirmationRequest) {
        // Lấy các thông tin từ request
        String vnp_TxnRef = confirmationRequest.getVnp_TxnRef();
        String vnp_ResponseCode = confirmationRequest.getVnp_ResponseCode();
//            String vnp_SecureHash = confirmationRequest.getVnp_SecureHash();
        BookedRoom bookedRoom = getBookingByTxnRef(vnp_TxnRef);
        // Kiểm tra ResponseCode (nếu là "00" thì là thành công)
        if (!"00".equals(vnp_ResponseCode)) {
            bookedRoom.setStatus(2);
            bookingRepository.save(bookedRoom);
            historyBookingService.updateStatusHistoryBooking(bookedRoom.getBookingId());
            return false;
        }
        bookedRoom.setStatus(1);
        bookingRepository.save(bookedRoom);
        historyBookingService.updateStatusHistoryBooking(bookedRoom.getBookingId());
        return true;
    }

    @Override
    public BookedRoom getBookingByTxnRef(String txnRef) {
        BookedRoom bookedRoom = bookingRepository.findBookingByTxnRef(txnRef);
        return bookedRoom;
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
        booking.setStatus(2);
        bookingRepository.save(booking);
    }

    @Override
    public BookedRoom getBookedRoomById(Long bookingId) {
        BookedRoom bookedRoom = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID " + bookingId));
        return bookedRoom;
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
