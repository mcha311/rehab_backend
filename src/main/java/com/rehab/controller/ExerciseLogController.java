package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.dto.exercise.CreateExerciseLogRequest;
import com.rehab.dto.exercise.ExerciseLogListResponse;
import com.rehab.dto.exercise.ExerciseLogResponse;
import com.rehab.service.exercise.ExerciseLogService;
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

/**
 * 운동 로그 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/exercise-logs")
@RequiredArgsConstructor
@Tag(name = "운동 로그", description = "운동 로그 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseLogController {

	private final ExerciseLogService exerciseLogService;

	/**
	 * 3.4 운동 로그 생성
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "운동 로그 생성", description = "운동 수행 기록을 생성합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<ExerciseLogResponse> createExerciseLog(
		@AuthenticationPrincipal User user,
		@Parameter(description = "운동 로그 생성 요청", required = true)
		@Valid @RequestBody CreateExerciseLogRequest request
	) {
		log.info("API 호출: 운동 로그 생성 - userId: {}, planItemId: {}", user.getUserId(), request.getPlanItemId());

		ExerciseLogResponse response = exerciseLogService.createExerciseLog(user.getUserId(), request);
		return ApiResponse.onSuccess(response);
	}

	/**
	 * 3.5 특정 날짜 운동 로그 조회
	 */
	@GetMapping
	@Operation(summary = "특정 날짜 운동 로그 조회", description = "특정 날짜의 운동 로그 목록을 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<ExerciseLogListResponse> getExerciseLogs(
		@AuthenticationPrincipal User user,
		@Parameter(description = "조회할 날짜 (YYYY-MM-DD)", required = true, example = "2025-12-01")
		@RequestParam
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		log.info("API 호출: 운동 로그 조회 - userId: {}, date: {}", user.getUserId(), date);

		ExerciseLogListResponse response = exerciseLogService.getExerciseLogsByDate(user.getUserId(), date);
		return ApiResponse.onSuccess(response);
	}
}
