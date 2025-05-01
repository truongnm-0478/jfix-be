package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.Paragraph;
import com.dut.jfix_be.enums.JlptLevel;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Integer> {
    List<Paragraph> findByLevel(JlptLevel level);
} 