package com.dut.jfix_be.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMistakeHistoryDTO {
    // Thông tin lần mắc lỗi đầu tiên
    private String userInput;
    private String correctAnswer;
    private LocalDateTime identifiedAt;
    private String feedbackProvided;
    private Boolean wasCorrected;

    // Thông tin thống kê
    private Float frequency;
    private Float improvementRate;
    private LocalDateTime lastOccurrence;

    // Danh sách các lần sửa lỗi
    private List<CorrectionHistoryDTO> correctionHistories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CorrectionHistoryDTO {
        private String correctionAttempt;
        private Boolean isCorrect;
        private Integer attemptNumber;
        private LocalDateTime errorCorrectionTime;
    }
} 