package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.FreeTalkTopic;
import com.dut.jfix_be.enums.JlptLevel;

@Repository
public interface FreeTalkTopicRepository extends JpaRepository<FreeTalkTopic, Integer> {
    List<FreeTalkTopic> findByLevel(JlptLevel level);
} 