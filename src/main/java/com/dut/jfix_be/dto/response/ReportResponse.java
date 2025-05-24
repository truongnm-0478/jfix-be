package com.dut.jfix_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Integer itemId;
    private String itemType;
    private String message;
    private Integer id;
    private String createDate;
    private Boolean isRead;

    public ReportResponse(Integer id, Integer itemId, String itemType, String message, String createDate, Boolean isRead) {
        this.id = id;
        this.itemId = itemId;
        this.itemType = itemType;
        this.message = message;
        this.createDate = createDate;
        this.isRead = isRead;
    }
} 