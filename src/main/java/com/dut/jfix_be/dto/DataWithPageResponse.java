package com.dut.jfix_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataWithPageResponse<T> {
    private List<T> data;
    private long totalRecords;
    private int totalPages;
    private Integer nextPage;
    private Integer previousPage;
}