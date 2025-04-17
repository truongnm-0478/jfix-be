package com.dut.jfix_be.entity;

import com.dut.jfix_be.enums.JlptLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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