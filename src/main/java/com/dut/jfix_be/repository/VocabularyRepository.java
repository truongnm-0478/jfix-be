package com.dut.jfix_be.repository;

import com.dut.jfix_be.entity.Vocabulary;
import com.dut.jfix_be.enums.JlptLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {
    List<Vocabulary> findByLevel(JlptLevel level);
}
