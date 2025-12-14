package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.dto.exercise.facility.CreateFacilityRequest;
import com.rehab.dto.exercise.facility.CreateFacilityResponse;
import com.rehab.dto.exercise.facility.UpdateFacilityRequest;
import com.rehab.service.facility.FacilityManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 운동 시설 관리 컨트롤러 (CRUD)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/facilities/manage")
@RequiredArgsConstructor
@Tag(name = "운동 시설 관리", description = "운동 시설 생성/수정/삭제 API (관리자용)")
public class FacilityManagementController {

	private final FacilityManagementService facilityManagementService;

	/**
	 * 운동 시설 생성
	 */
	@PostMapping
	@Operation(summary = "운동 시설 생성", description = "새로운 운동 시설을 등록합니다.")
	public ApiResponse<CreateFacilityResponse> createFacility(
		@Valid @RequestBody CreateFacilityRequest request
	) {
		log.info("API 호출: 운동 시설 생성 - name: {}", request.getName());

		CreateFacilityResponse response = facilityManagementService.createFacility(request);

		return ApiResponse.onSuccess(response);
	}

	/**
	 * 운동 시설 수정
	 */
	@PutMapping("/{facilityId}")
	@Operation(summary = "운동 시설 수정", description = "기존 운동 시설 정보를 수정합니다.")
	public ApiResponse<CreateFacilityResponse> updateFacility(
		@Parameter(description = "시설 ID", required = true)
		@PathVariable Long facilityId,

		@RequestBody UpdateFacilityRequest request
	) {
		log.info("API 호출: 운동 시설 수정 - facilityId: {}", facilityId);

		CreateFacilityResponse response = facilityManagementService.updateFacility(facilityId, request);

		return ApiResponse.onSuccess(response);
	}

	/**
	 * 운동 시설 삭제 (비활성화)
	 */
	@DeleteMapping("/{facilityId}")
	@Operation(summary = "운동 시설 삭제", description = "운동 시설을 비활성화합니다. (실제 삭제는 아님)")
	public ApiResponse<Void> deleteFacility(
		@Parameter(description = "시설 ID", required = true)
		@PathVariable Long facilityId
	) {
		log.info("API 호출: 운동 시설 삭제 - facilityId: {}", facilityId);

		facilityManagementService.deleteFacility(facilityId);

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 운동 시설 검증 완료
	 */
	@PostMapping("/{facilityId}/verify")
	@Operation(summary = "운동 시설 검증", description = "운동 시설을 검증 완료 상태로 변경합니다.")
	public ApiResponse<Void> verifyFacility(
		@Parameter(description = "시설 ID", required = true)
		@PathVariable Long facilityId
	) {
		log.info("API 호출: 운동 시설 검증 - facilityId: {}", facilityId);

		facilityManagementService.verifyFacility(facilityId);

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 운동 시설 활성화
	 */
	@PostMapping("/{facilityId}/activate")
	@Operation(summary = "운동 시설 활성화", description = "비활성화된 시설을 다시 활성화합니다.")
	public ApiResponse<Void> activateFacility(
		@Parameter(description = "시설 ID", required = true)
		@PathVariable Long facilityId
	) {
		log.info("API 호출: 운동 시설 활성화 - facilityId: {}", facilityId);

		facilityManagementService.activateFacility(facilityId);

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 대량 시설 생성 (배치)
	 */
	@PostMapping("/batch")
	@Operation(summary = "대량 시설 생성", description = "여러 시설을 한 번에 등록합니다. (배치용)")
	public ApiResponse<List<CreateFacilityResponse>> createFacilitiesBatch(
		@RequestBody List<@Valid CreateFacilityRequest> requests
	) {
		log.info("API 호출: 대량 시설 생성 - count: {}", requests.size());

		List<CreateFacilityResponse> responses = facilityManagementService.createFacilitiesBatch(requests);

		return ApiResponse.onSuccess(responses);
	}
}
