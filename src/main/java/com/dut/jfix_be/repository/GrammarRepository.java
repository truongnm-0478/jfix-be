package com.dut.jfix_be.repository;

import com.dut.jfix_be.entity.Grammar;
import com.dut.jfix_be.enums.JlptLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {
    List<Grammar> findByLevel(JlptLevel level);

    @Query("SELECT COUNT(g) FROM Grammar g WHERE g.level = :level AND g.deleteDate IS NULL")
    long countByLevel(@Param("level") JlptLevel level);

    @Query("SELECT g FROM Grammar g WHERE g.level = :level AND g.deleteDate IS NULL ORDER BY g.id ASC")
    Page<Grammar> findByLevelWithPageable(@Param("level") JlptLevel level, Pageable pageable);

}
