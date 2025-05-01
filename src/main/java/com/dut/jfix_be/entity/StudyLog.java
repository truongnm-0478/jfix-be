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
@Table(name = "study_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "card_id", nullable = false)
    private Integer cardId;

    @Column(name = "review_date", nullable = false)
    @Builder.Default
    private LocalDateTime reviewDate = LocalDateTime.now();

    @Column(name = "repetition", nullable = false)
    @Builder.Default
    private Integer repetition = 0;

    @Column(name = "intervals", nullable = false)
    @Builder.Default
    private Float intervals = 0f;

    @Column(name = "easiness_factor", nullable = false)
    @Builder.Default
    private Float easinessFactor = 2.5f;

    @Column(name = "last_review_date")
    private LocalDateTime lastReviewDate;

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