package com.dut.jfix_be.entity;

import com.dut.jfix_be.enums.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deck_id")
    private Integer deckId;

    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(columnDefinition = "float default 1")
    private Float weight = 1.0f;

    @Column(name = "last_review_date")
    private LocalDateTime lastReviewDate;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;

    @Column(name = "review_count", columnDefinition = "integer default 0")
    private Integer reviewCount = 0;

    @Column(name = "ease_factor", columnDefinition = "float default 2.5")
    private Float easeFactor = 2.5f;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "create_by", nullable = false)
    private String createBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_by")
    private String deleteBy;
}