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
@Table(name = "user_mistakes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMistake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "card_id", nullable = false)
    private Integer cardId;

    @Column(name = "user_input", nullable = false)
    private String userInput;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "identified_at")
    @Builder.Default
    private LocalDateTime identifiedAt = LocalDateTime.now();

    @Column(name = "feedback_provided")
    private String feedbackProvided;

    @Column(name = "was_corrected", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean wasCorrected = false;

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