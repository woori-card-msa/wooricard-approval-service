package com.wooricard.approval.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "승인 취소 요청")
public class CancelRequest {

    @Schema(description = "원거래 ID", example = "TXN-CP-SUCCESS-001", required = true)
    private String transactionId;

    @Schema(description = "카드 번호 (16자리)", example = "4111111111111111", required = true)
    private String cardNumber;

    @Schema(description = "취소 사유", example = "고객 요청")
    private String cancelReason;
}
