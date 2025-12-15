package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.dto.medication.CreateMedicationLogRequest;
import com.rehab.dto.medication.MedicationLogListResponse;
import com.rehab.dto.medication.MedicationLogResponse;
import com.rehab.service.medicationService.MedicationLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/medication-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "복약 로그", description = "복약 로그 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class MedicationLogController {

	private final MedicationLogService medicationLogService;

	/**
	 * 4.5 복약 로그 생성
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "복약 로그 생성", description = "복약 로그를 생성합니다. 생성 시 자동으로 일일 요약이 업데이트됩니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<MedicationLogResponse> createMedicationLog(
		@AuthenticationPrincipal User user,
		@Parameter(description = "복약 로그 생성 요청", required = true)
		@Valid @RequestBody CreateMedicationLogRequest request
	) {
		log.info("API 호출: 복약 로그 생성 - userId: {}, medicationId: {}", user.getUserId(), request.getMedicationId());

		MedicationLogResponse response = medicationLogService.createMedicationLog(user.getUserId(), request);
		return ApiResponse.onSuccess(response);
	}

	/**
	 * 특정 날짜 복약 로그 조회
	 */
	@GetMapping
	@Operation(summary = "특정 날짜 복약 로그 조회", description = "특정 날짜의 복약 로그 목록을 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<MedicationLogListResponse> getMedicationLogs(
		@AuthenticationPrincipal User user,
		@Parameter(description = "조회할 날짜 (YYYY-MM-DD)", required = true, example = "2025-12-01")
		@RequestParam
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		log.info("API 호출: 복약 로그 조회 - userId: {}, date: {}", user.getUserId(), date);

		MedicationLogListResponse response = medicationLogService.getMedicationLogsByDate(user.getUserId(), date);
		return ApiResponse.onSuccess(response);
	}
}
