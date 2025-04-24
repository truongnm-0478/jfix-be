package com.dut.jfix_be.entity;

import com.dut.jfix_be.enums.JlptLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grammars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grammar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String romaji;

    @Column(nullable = false)
    private String structure;

    @Column(columnDefinition = "TEXT")
    private String usage;

    private String meaning;

    private String example;

    @Column(name = "example_meaning")
    private String exampleMeaning;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private JlptLevel level = JlptLevel.FREE;

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
