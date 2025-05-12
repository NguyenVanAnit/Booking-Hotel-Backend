package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByRole(Integer role);
    List<Staff> findByStatus(Integer status);

    // Optional search
    List<Staff> findByFullNameContainingIgnoreCase(String name);
    List<Staff> findByEmailContainingIgnoreCase(String email);

    Optional<Staff> findByPhoneNumberAndPassword(String phoneNumber, String password);
}
