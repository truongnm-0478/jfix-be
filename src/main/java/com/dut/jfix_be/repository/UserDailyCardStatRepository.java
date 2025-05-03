package com.dut.jfix_be.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.UserDailyCardStat;

@Repository
public interface UserDailyCardStatRepository extends JpaRepository<UserDailyCardStat, Integer> {
    Optional<UserDailyCardStat> findByUserIdAndStatDate(Integer userId, LocalDate statDate);
} 