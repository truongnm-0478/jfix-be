package com.dut.jfix_be.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.UserMistake;

@Repository
public interface UserMistakeRepository extends JpaRepository<UserMistake, Integer> {
    Optional<UserMistake> findByUserIdAndCardId(Integer userId, Integer cardId);
    List<UserMistake> findByCardId(Integer cardId);
    void deleteAllByCardId(Integer cardId);
} 