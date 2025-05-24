package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("SELECT r FROM Report r WHERE r.isRead = false ORDER BY r.createDate DESC")
    List<Report> findAllUnreadReports();

    @Query("SELECT r FROM Report r ORDER BY r.createDate DESC")
    List<Report> findAllReportsOrderByCreateDateDesc();
} 