package com.dut.jfix_be.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dut.jfix_be.enums.JlptLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_goals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_level")
    private JlptLevel targetLevel;

    private String description;

    @Column(name = "daily_minutes")
    private Integer dailyMinutes;

    @Column(name = "daily_vocab_target")
    private Integer dailyVocabTarget;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "create_date", nullable = false)
    @Builder.Default
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