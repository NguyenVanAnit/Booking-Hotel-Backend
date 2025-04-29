package com.nguyenvanancodeweb.lakesidehotel.repository;

import com.nguyenvanancodeweb.lakesidehotel.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByStaffIdAndDateBetween(Long staffId, LocalDate start, LocalDate end);

    @Query("SELECT DISTINCT a.staffId FROM AttendanceRecord a WHERE a.date = :today AND a.isAbsent = false")
    List<Long> findStaffIdsCheckedInToday(@Param("today") LocalDate today);

    @Query("SELECT DISTINCT a.staffId FROM AttendanceRecord a WHERE a.date = :today AND a.checkOutTime IS NOT NULL")
    List<Long> findStaffIdsCheckedOutToday(@Param("today") LocalDate today);

    @Query("SELECT COUNT(a) FROM AttendanceRecord a WHERE a.staffId = :staffId AND a.date BETWEEN :start AND :end AND a.isAbsent = false")
    long countWorkingDays(@Param("staffId") Long staffId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a.date FROM AttendanceRecord a WHERE a.staffId = :staffId AND a.date BETWEEN :start AND :end AND a.isAbsent = false")
    List<LocalDate> findAbsentDates(@Param("staffId") Long staffId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(a) FROM AttendanceRecord a WHERE a.staffId = :staffId AND a.isAbsent = false AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    int countPresentDays(@Param("staffId") Long staffId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(a) FROM AttendanceRecord a WHERE a.staffId = :staffId AND a.isAbsent = true AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    int countAbsentDays(@Param("staffId") Long staffId, @Param("month") int month, @Param("year") int year);


}
