package com.wooricard.approval.controller;

import com.wooricard.approval.dto.MonthlyApprovalDto;
import com.wooricard.approval.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/authorization")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Billing", description = "청구 API")
public class BillingController {

    private final AuthorizationService authorizationService;

    @Operation(summary = "월별 승인 내역 조회", description = "카드번호와 월 기준으로 APPROVED 승인 내역을 조회합니다.")
    @GetMapping("/approved/monthly")
    public ResponseEntity<List<MonthlyApprovalDto>> getMonthlyApproved(
            @RequestParam String cardNumberMasked,
            @RequestParam String month) {

        YearMonth yearMonth = YearMonth.parse(month);
        List<MonthlyApprovalDto> result = authorizationService.getMonthlyApprovedByCard(cardNumberMasked, yearMonth);
        return ResponseEntity.ok(result);
    }
}
