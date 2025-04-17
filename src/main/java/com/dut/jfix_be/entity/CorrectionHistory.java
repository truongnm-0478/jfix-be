package com.dut.jfix_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "correction_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_mistake_id", nullable = false)
    private Integer userMistakeId;

    @Column(name = "correction_attempt")
    private String correctionAttempt;

    @Column(name = "is_correct", columnDefinition = "boolean default false")
    private Boolean isCorrect = false;

    @Column(name = "error_correction_time")
    private LocalDateTime errorCorrectionTime = LocalDateTime.now();

    @Column(name = "attempt_number", columnDefinition = "integer default 1")
    private Integer attemptNumber = 1;

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