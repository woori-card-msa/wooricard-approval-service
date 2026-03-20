package com.wooricard.approval.dto;

import java.math.BigDecimal;

public class RefundRequest {
    private String cardNumber;
    private BigDecimal amount;
    private String transactionId;

    public RefundRequest() {}

    public RefundRequest(String cardNumber, BigDecimal amount, String transactionId) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
