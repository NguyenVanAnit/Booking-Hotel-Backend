package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService implements IStaffService{
    private final StaffRepository staffRepository;

    @Override
    public Staff addStaff(Staff staff) {
        staff.setId(null); // Auto-generate
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Long id, Staff updated) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.setFullName(updated.getFullName());
        staff.setPhoneNumber(updated.getPhoneNumber());
        staff.setEmail(updated.getEmail());
        staff.setPassword(updated.getPassword());
        staff.setHireDate(updated.getHireDate());
        staff.setSalary(updated.getSalary());
        staff.setPosition(updated.getPosition());
        staff.setRole(updated.getRole());
        staff.setStatus(updated.getStatus());

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
}
