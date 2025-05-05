package com.dut.jfix_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRateResponse {
    private int correct;
    private int incorrect;
    private double errorRate;
} 