package com.dut.jfix_be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.LearningGoal;
import com.dut.jfix_be.enums.JlptLevel;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal, Integer> {
    
    @Query("SELECT lg FROM LearningGoal lg WHERE lg.userId = :userId AND lg.deleteDate IS NULL")
    Optional<LearningGoal> findActiveGoalByUserId(Integer userId);
    
    @Query("SELECT CASE WHEN COUNT(lg) > 0 THEN true ELSE false END FROM LearningGoal lg " +
           "WHERE lg.userId = :userId AND (:level IS NULL OR lg.targetLevel = :level) AND lg.deleteDate IS NULL")
    boolean existsActiveGoalByUserIdAndLevel(Integer userId, JlptLevel level);
    
    @Query("SELECT lg FROM LearningGoal lg WHERE lg.userId = :userId AND lg.targetLevel = :level AND lg.deleteDate IS NULL")
    Optional<LearningGoal> findActiveGoalByUserIdAndLevel(Integer userId, JlptLevel level);
}