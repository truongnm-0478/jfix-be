package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.CorrectionHistory;

@Repository
public interface CorrectionHistoryRepository extends JpaRepository<CorrectionHistory, Integer> {
    int countByUserMistakeId(Integer userMistakeId);
    List<CorrectionHistory> findAllByUserMistakeId(Integer userMistakeId);
    void deleteAllByUserMistakeId(Integer userMistakeId);
} 