package com.wooricard.approval.dto;

import com.wooricard.approval.entity.Authorization;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class MonthlyApprovalDto {

    private String transactionId;
    private String cardNumberMasked;
    private BigDecimal amount;
    private String merchantId;
    private LocalDateTime authorizationDate;
    private String status;

    public static MonthlyApprovalDto from(Authorization authorization) {
        return MonthlyApprovalDto.builder()
                .transactionId(authorization.getTransactionId())
                .cardNumberMasked(authorization.getCardNumberMasked())
                .amount(authorization.getAmount())
                .merchantId(authorization.getMerchantId())
                .authorizationDate(authorization.getAuthorizationDate())
                .status(authorization.getStatus().name())
                .build();
    }
}
