package com.dut.jfix_be.entity;

import com.dut.jfix_be.enums.JlptLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vocabularies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String word;

    private String reading;

    private String meaning;

    @Column(name = "example_with_reading")
    private String exampleWithReading;

    @Column(name = "example_without_reading")
    private String exampleWithoutReading;

    @Column(name = "example_meaning")
    private String exampleMeaning;

    private String audio;

    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level")
    private JlptLevel level = JlptLevel.FREE;

    private String chapter;

    private String section;

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