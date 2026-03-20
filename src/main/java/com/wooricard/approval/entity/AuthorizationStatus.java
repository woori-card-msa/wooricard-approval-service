package com.wooricard.approval.entity;

/**
 * 승인 상태
 */
public enum AuthorizationStatus {
    /**
     * 승인
     */
    APPROVED,
    
    /**
     * 거절
     */
    REJECTED,
    
    /**
     * 매출 확정
     */
    CONFIRMED,

    /**
     * 승인 취소
     */
    CANCELLED
}
