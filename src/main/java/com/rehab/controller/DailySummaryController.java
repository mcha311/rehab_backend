package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.dto.dailySummary.DailySummaryResponse;
import com.rehab.service.dailySummary.DailySummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 일일 요약 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/daily-summary")
@RequiredArgsConstructor
@Tag(name = "일일 요약", description = "일일 요약 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class DailySummaryController {

	private final DailySummaryService dailySummaryService;

	/**
	 * 3.6 일일 요약 조회
	 */
	@GetMapping
	@Operation(summary = "일일 요약 조회", description = "특정 날짜의 운동/복약 완료율 및 통증 요약을 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<DailySummaryResponse> getDailySummary(
		@AuthenticationPrincipal User user,
		@Parameter(description = "조회할 날짜 (YYYY-MM-DD)", required = true, example = "2025-12-01")
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		log.info("API 호출: 일일 요약 조회 - userId: {}, date: {}", user.getUserId(), date);

		DailySummaryResponse response = dailySummaryService.getDailySummary(user.getUserId(), date);
		return ApiResponse.onSuccess(response);
	}
}
