package com.dut.jfix_be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dut.jfix_be.entity.Card;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.Skill;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByDeckId(Integer deckId);
    
    @Query("SELECT c FROM Card c WHERE c.deckId = :deckId AND c.deleteDate IS NULL")
    List<Card> findActiveByDeckId(Integer deckId);
    
    @Query("SELECT c FROM Card c WHERE c.deckId = :deckId AND c.type = :type AND c.skill = :skill AND c.deleteDate IS NULL")
    List<Card> findActiveByDeckIdAndTypeAndSkill(Integer deckId, CardType type, Skill skill);

    List<Card> findByTypeAndItemId(CardType type, Integer itemId);
}
