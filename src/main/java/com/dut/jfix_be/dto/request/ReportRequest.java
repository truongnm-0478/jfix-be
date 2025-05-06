package com.dut.jfix_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequest {
    @NotNull(message = "validation.card.id.required")
    private Integer cardId;

    @NotBlank(message = "validation.report.content.required")
    private String content;
} 