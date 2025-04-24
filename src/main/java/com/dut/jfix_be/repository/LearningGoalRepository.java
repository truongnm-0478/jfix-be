package com.dut.jfix_be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.LearningGoal;
import com.dut.jfix_be.enums.JlptLevel;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal, Long> {
    
    @Query("SELECT lg FROM LearningGoal lg WHERE lg.userId = :userId AND lg.deleteDate IS NULL")
    Optional<LearningGoal> findActiveGoalByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(lg) > 0 FROM LearningGoal lg WHERE lg.userId = :userId AND lg.targetLevel = :level AND lg.deleteDate IS NULL")
    boolean existsActiveGoalByUserIdAndLevel(@Param("userId") Integer userId, @Param("level") JlptLevel level);
}