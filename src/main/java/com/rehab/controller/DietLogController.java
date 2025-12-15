package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.dto.diet.CreateDietLogRequest;
import com.rehab.dto.diet.DietLogResponse;
import com.rehab.service.dietService.DietLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/diet-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "식단 로그 API", description = "식단 로그 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class DietLogController {

	private final DietLogService dietLogService;

	@PostMapping
	@Operation(summary = "식단 로그 생성", description = "식단 로그를 생성합니다. 생성 시 자동으로 일일 요약이 업데이트됩니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<DietLogResponse> createDietLog(
		@AuthenticationPrincipal User user,
		@Parameter(description = "식단 로그 생성 요청", required = true)
		@Valid @RequestBody CreateDietLogRequest request
	) {
		log.info("POST /api/v1/diet-logs - userId: {}, dietPlanItemId: {}", user.getUserId(), request.getDietPlanItemId());

		DietLogResponse response = dietLogService.createDietLog(user.getUserId(), request);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping
	@Operation(summary = "식단 로그 조회", description = "특정 날짜의 식단 로그를 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<DietLogResponse>> getDietLogs(
		@AuthenticationPrincipal User user,
		@Parameter(description = "조회 날짜 (YYYY-MM-DD)", required = true, example = "2025-12-11")
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		log.info("GET /api/v1/diet-logs - userId: {}, date: {}", user.getUserId(), date);

		List<DietLogResponse> response = dietLogService.getDietLogsByDate(user.getUserId(), date);
		return ApiResponse.onSuccess(response);
	}
}
