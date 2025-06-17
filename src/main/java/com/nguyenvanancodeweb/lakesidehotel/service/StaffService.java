package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.exception.ResourceNotFoundException;
import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.repository.StaffRepository;
import com.nguyenvanancodeweb.lakesidehotel.response.StaffAttendanceSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService{
    private final StaffRepository staffRepository;

    @Override
    public Staff addStaff(Staff staff) {
        staff.setStatus(1);
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Long id, Staff updated) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if(updated.getFullName() != null) staff.setFullName(updated.getFullName());
        if(updated.getPhoneNumber() != null) staff.setPhoneNumber(updated.getPhoneNumber());
        if(updated.getEmail() != null) staff.setEmail(updated.getEmail());
        if(updated.getPassword() != null) staff.setPassword(updated.getPassword());
        if(updated.getHireDate() != null) staff.setHireDate(updated.getHireDate());
        if(updated.getSalary() != null) staff.setSalary(updated.getSalary());
        if(updated.getDepartment() != null) staff.setDepartment(updated.getDepartment());
        if(updated.getRole() != null) staff.setRole(updated.getRole());
        if(updated.getStatus() != null) staff.setStatus(updated.getStatus());
        if(updated.getGender() != null) staff.setGender(updated.getGender());
        if(updated.getAddress() != null) staff.setAddress(updated.getAddress());
        if(updated.getBirthDate() != null) staff.setBirthDate(updated.getBirthDate());

        return staffRepository.save(staff);
    }

    @Override
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public void chageStatus(Long id, Integer status) {
        Staff staff = getStaffById(id);
        staff.setStatus(status);
    }

    @Override
    public List<Staff> getByRole(Integer role) {
        return staffRepository.findByRole(role);
    }

    @Override
    public List<Staff> getByStatus(Integer status) {
        return staffRepository.findByStatus(status);
    }

    @Override
    public List<Staff> searchByName(String name) {
        return staffRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public long countByRole(Integer role) {
        return staffRepository.findAll().stream()
                .filter(s -> s.getRole().equals(role)).count();
    }

    @Override
    public long countByStatus(Integer status) {
        return staffRepository.findAll().stream()
                .filter(s -> s.getStatus().equals(status)).count();
    }

    public StaffAttendanceSummaryDto login(String phoneNumber, String password) {
        Optional<Staff> staffOptional = staffRepository.findByPhoneNumberAndPassword(phoneNumber, password);

        if (staffOptional.isEmpty()) {
            throw new ResourceNotFoundException("Không tồn tại số điện thoại" + phoneNumber);
        }

        Staff staff = staffOptional.get();

        if (!staff.getPassword().equals(password)) {
            throw new ResourceNotFoundException("Số điện thoại hoặc mật khẩu không đúng");
        }

        StaffAttendanceSummaryDto staffResponse = new StaffAttendanceSummaryDto();
        staffResponse.setStaffId(staffOptional.get().getId());
        staffResponse.setFullName(staffOptional.get().getFullName());
        staffResponse.setPhoneNumber(staffOptional.get().getPhoneNumber());
        staffResponse.setEmail(staffOptional.get().getEmail());
        staffResponse.setDepartment(staffOptional.get().getDepartment());

        return staffResponse;

    }
}
