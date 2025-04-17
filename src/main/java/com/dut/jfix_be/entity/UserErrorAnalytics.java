package com.dut.jfix_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_error_analytics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserErrorAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "error_type_id", nullable = false)
    private Integer errorTypeId;

    private Float frequency;

    @Column(name = "improvement_rate")
    private Float improvementRate;

    @Column(name = "last_occurrence", nullable = false)
    private LocalDateTime lastOccurrence;

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