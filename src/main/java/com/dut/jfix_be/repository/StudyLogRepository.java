package com.dut.jfix_be.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.StudyLog;

@Repository
public interface StudyLogRepository extends JpaRepository<StudyLog, Integer> {
    @Query("SELECT sl FROM StudyLog sl WHERE sl.userId = :userId AND sl.reviewDate >= :startDate AND sl.reviewDate < :endDate")
    List<StudyLog> findByUserIdAndReviewDate(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sl FROM StudyLog sl WHERE sl.userId = :userId AND sl.reviewDate <= :endDate")
    List<StudyLog> findByUserIdAndReviewDateUpTo(Integer userId, LocalDateTime endDate);
} 