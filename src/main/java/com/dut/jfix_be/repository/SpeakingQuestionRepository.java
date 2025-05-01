package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.SpeakingQuestion;
import com.dut.jfix_be.enums.JlptLevel;

@Repository
public interface SpeakingQuestionRepository extends JpaRepository<SpeakingQuestion, Integer> {
    List<SpeakingQuestion> findByLevel(JlptLevel level);
} 