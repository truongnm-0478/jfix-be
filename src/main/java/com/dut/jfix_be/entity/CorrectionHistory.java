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
    @Builder.Default
    private Boolean isCorrect = false;

    @Column(name = "error_correction_time")
    @Builder.Default
    private LocalDateTime errorCorrectionTime = LocalDateTime.now();

    @Column(name = "attempt_number", columnDefinition = "integer default 1")
    @Builder.Default
    private Integer attemptNumber = 1;

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