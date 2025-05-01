package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dut.jfix_be.entity.Sentence;
import com.dut.jfix_be.enums.JlptLevel;

public interface SentenceRepository extends JpaRepository<Sentence, Integer> {
    List<Sentence> findByLevel(JlptLevel level);
}