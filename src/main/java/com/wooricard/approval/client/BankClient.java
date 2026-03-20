package com.wooricard.approval.client;

import com.wooricard.approval.dto.BalanceResponse;
import com.wooricard.approval.dto.DebitResponse;
import com.wooricard.approval.dto.RefundResponse;

import java.math.BigDecimal;

/**
 * 은행 서비스 클라이언트 인터페이스
 */
public interface BankClient {
    
    /**
     * 은행 잔액 조회
     * @param cardNumber 카드 번호
     * @param amount 요청 금액
     * @return 잔액 조회 응답
     */
    BalanceResponse checkBalance(String cardNumber, BigDecimal amount);
    
    /**
     * 은행 출금 요청
     * @param cardNumber 카드 번호
     * @param amount 출금 금액
     * @param transactionId 거래 ID
     * @return 출금 응답
     */
    DebitResponse requestDebit(String cardNumber, BigDecimal amount, String transactionId);

    /**
     * 은행 환불 요청 (체크카드 취소 시)
     * @param cardNumber 카드 번호
     * @param amount 환불 금액
     * @param transactionId 원거래 ID
     * @return 환불 응답
     */
    RefundResponse requestRefund(String cardNumber, BigDecimal amount, String transactionId);
}
