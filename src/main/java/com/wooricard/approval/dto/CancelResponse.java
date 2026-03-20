package com.wooricard.approval.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "승인 취소 응답")
public class CancelResponse {

    @Schema(description = "원거래 ID")
    private String transactionId;

    @Schema(description = "응답 코드 (00: 취소 성공)")
    private String responseCode;

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "취소 금액")
    private BigDecimal amount;

    @Schema(description = "취소 처리 일시")
    private LocalDateTime cancelDate;

    @Schema(description = "취소 성공 여부")
    private boolean cancelled;
}
