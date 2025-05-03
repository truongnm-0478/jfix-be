package com.dut.jfix_be.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_daily_card_stats", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "stat_date"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDailyCardStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "card_count", nullable = false)
    private Integer cardCount;
} 