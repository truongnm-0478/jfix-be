package com.dut.jfix_be.repository;

import com.dut.jfix_be.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByDeckId(Integer deckId);
}
