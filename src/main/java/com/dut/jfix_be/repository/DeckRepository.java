package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.Deck;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {
    List<Deck> findByUserId(Integer userId);
    
    @Query("SELECT d FROM Deck d WHERE d.userId = :userId AND d.deleteDate IS NULL")
    List<Deck> findActiveByUserId(Integer userId);
} 