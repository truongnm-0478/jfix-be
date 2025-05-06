package com.dut.jfix_be.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "card_id", nullable = false)
    private Integer cardId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;
} 