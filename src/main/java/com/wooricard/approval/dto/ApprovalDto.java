package com.wooricard.approval.dto;

import com.wooricard.approval.entity.Authorization;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ApprovalDto {

    private Long id;
    private String transactionId;
    private String merchantId;
    private BigDecimal amount;
    private String responseCode;
    private LocalDateTime authorizationDate;

    public static ApprovalDto from(Authorization authorization) {
        return ApprovalDto.builder()
                .id(authorization.getId())
                .transactionId(authorization.getTransactionId())
                .merchantId(authorization.getMerchantId())
                .amount(authorization.getAmount())
                .responseCode(authorization.getResponseCode())
                .authorizationDate(authorization.getAuthorizationDate())
                .build();
    }
}
