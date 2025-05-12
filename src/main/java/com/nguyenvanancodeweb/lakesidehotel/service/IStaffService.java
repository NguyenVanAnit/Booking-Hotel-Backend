package com.nguyenvanancodeweb.lakesidehotel.service;

import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import com.nguyenvanancodeweb.lakesidehotel.response.StaffAttendanceSummaryDto;

import java.util.List;

public interface IStaffService {
    Staff addStaff(Staff staff);
    Staff updateStaff(Long id, Staff updatedStaff);
    void deleteStaff(Long id);
    Staff getStaffById(Long id);
    List<Staff> getAllStaff();
    void chageStatus(Long id, Integer status);

    List<Staff> getByRole(Integer role);
    List<Staff> getByStatus(Integer status);
    List<Staff> searchByName(String name);

    long countByRole(Integer role);
    long countByStatus(Integer status);

    StaffAttendanceSummaryDto login(String phoneNumber, String password);
}
