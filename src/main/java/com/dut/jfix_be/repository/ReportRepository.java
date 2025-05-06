package com.dut.jfix_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
} 