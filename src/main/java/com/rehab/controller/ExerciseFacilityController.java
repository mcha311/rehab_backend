package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.domain.entity.enums.FacilityType;
import com.rehab.dto.exercise.facility.AddFavoriteRequest;
import com.rehab.dto.exercise.facility.FacilityDetailResponse;
import com.rehab.dto.exercise.facility.FacilityFavoriteResponse;
import com.rehab.dto.exercise.facility.FacilityListResponse;
import com.rehab.dto.exercise.facility.FacilitySearchResponse;
import com.rehab.dto.exercise.facility.UpdateFavoriteMemoRequest;
import com.rehab.service.exercise.ExerciseFacilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 운동 시설 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
@Tag(name = "운동 시설", description = "주변 운동 시설 조회 및 즐겨찾기 API")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseFacilityController {

	private final ExerciseFacilityService facilityService;

	/**
	 * 주변 시설 검색
	 */
	@GetMapping("/nearby")
	@Operation(summary = "주변 시설 검색", description = "사용자 주소 기준 반경 내 운동 시설을 검색합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<FacilitySearchResponse>> searchNearbyFacilities(
		@AuthenticationPrincipal User user,
		@Parameter(description = "검색 반경 (km)", example = "5.0")
		@RequestParam(value = "radius", defaultValue = "5.0") Double radiusKm,

		@Parameter(description = "시설 유형 필터 (복수 선택 가능)")
		@RequestParam(value = "types", required = false) List<FacilityType> facilityTypes
	) {
		log.info("API 호출: 주변 시설 검색 - userId: {}, radius: {}km, types: {}",
			user.getUserId(), radiusKm, facilityTypes);

		List<FacilitySearchResponse> results =
			facilityService.searchNearbyFacilities(user.getUserId(), radiusKm, facilityTypes);

		return ApiResponse.onSuccess(results);
	}

	/**
	 * 재활에 적합한 시설 검색
	 */
	@GetMapping("/nearby/rehab")
	@Operation(summary = "재활 적합 시설 검색", description = "재활에 적합한 시설만 검색합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<FacilitySearchResponse>> searchRehabFacilities(
		@AuthenticationPrincipal User user,
		@Parameter(description = "검색 반경 (km)", example = "10.0")
		@RequestParam(value = "radius", defaultValue = "10.0") Double radiusKm
	) {
		log.info("API 호출: 재활 시설 검색 - userId: {}, radius: {}km", user.getUserId(), radiusKm);

		List<FacilitySearchResponse> results =
			facilityService.searchRehabSuitableFacilities(user.getUserId(), radiusKm);

		return ApiResponse.onSuccess(results);
	}

	/**
	 * 공공 시설 검색
	 */
	@GetMapping("/nearby/public")
	@Operation(summary = "공공 시설 검색", description = "무료/저렴한 공공 시설만 검색합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<FacilitySearchResponse>> searchPublicFacilities(
		@AuthenticationPrincipal User user,
		@Parameter(description = "검색 반경 (km)", example = "5.0")
		@RequestParam(value = "radius", defaultValue = "5.0") Double radiusKm
	) {
		log.info("API 호출: 공공 시설 검색 - userId: {}, radius: {}km", user.getUserId(), radiusKm);

		List<FacilitySearchResponse> results =
			facilityService.searchPublicFacilities(user.getUserId(), radiusKm);

		return ApiResponse.onSuccess(results);
	}

	/**
	 * 시설 상세 조회
	 */
	@GetMapping("/{facilityId}")
	@Operation(summary = "시설 상세 조회", description = "특정 시설의 상세 정보를 조회합니다. 인증된 사용자 정보를 자동으로 추출하여 즐겨찾기 여부를 확인합니다.")
	public ApiResponse<FacilityDetailResponse> getFacilityDetail(
		@AuthenticationPrincipal User user,
		@Parameter(description = "시설 ID", required = true)
		@PathVariable Long facilityId
	) {
		log.info("API 호출: 시설 상세 조회 - facilityId: {}, userId: {}", facilityId, user.getUserId());

		FacilityDetailResponse result = facilityService.getFacilityDetail(facilityId, user.getUserId());

		return ApiResponse.onSuccess(result);
	}

	/**
	 * 시설 유형별 검색
	 */
	@GetMapping("/by-type/{facilityType}")
	@Operation(summary = "시설 유형별 검색", description = "특정 유형의 시설만 검색합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<FacilitySearchResponse>> searchByType(
		@AuthenticationPrincipal User user,
		@Parameter(description = "시설 유형", required = true)
		@PathVariable FacilityType facilityType,

		@Parameter(description = "검색 반경 (km)", example = "5.0")
		@RequestParam(value = "radius", defaultValue = "5.0") Double radiusKm
	) {
		log.info("API 호출: 시설 유형별 검색 - userId: {}, type: {}, radius: {}km",
			user.getUserId(), facilityType, radiusKm);

		List<FacilitySearchResponse> results =
			facilityService.searchByType(user.getUserId(), facilityType, radiusKm);

		return ApiResponse.onSuccess(results);
	}

	/**
	 * 지역별 시설 검색
	 */
	@GetMapping("/by-district/{district}")
	@Operation(summary = "지역별 시설 검색", description = "특정 지역의 시설을 검색합니다.")
	public ApiResponse<List<FacilityListResponse>> searchByDistrict(
		@Parameter(description = "시/군/구", required = true, example = "강남구")
		@PathVariable String district
	) {
		log.info("API 호출: 지역별 시설 검색 - district: {}", district);

		List<FacilityListResponse> results = facilityService.searchByDistrict(district);

		return ApiResponse.onSuccess(results);
	}

	// ================================
	// 즐겨찾기 관련 API
	// ================================

	/**
	 * 즐겨찾기 추가
	 */
	@PostMapping("/favorites")
	@Operation(summary = "즐겨찾기 추가", description = "시설을 즐겨찾기에 추가합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Void> addFavorite(
		@AuthenticationPrincipal User user,
		@RequestBody AddFavoriteRequest request
	) {
		log.info("API 호출: 즐겨찾기 추가 - userId: {}, facilityId: {}",
			user.getUserId(), request.getFacilityId());

		facilityService.addFavorite(user.getUserId(), request.getFacilityId(), request.getMemo());

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 즐겨찾기 목록 조회
	 */
	@GetMapping("/favorites")
	@Operation(summary = "즐겨찾기 목록 조회", description = "사용자의 즐겨찾기 시설 목록을 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<List<FacilityFavoriteResponse>> getFavorites(
		@AuthenticationPrincipal User user
	) {
		log.info("API 호출: 즐겨찾기 목록 조회 - userId: {}", user.getUserId());

		List<FacilityFavoriteResponse> results = facilityService.getFavorites(user.getUserId());

		return ApiResponse.onSuccess(results);
	}

	/**
	 * 즐겨찾기 삭제
	 */
	@DeleteMapping("/favorites/{favoriteId}")
	@Operation(summary = "즐겨찾기 삭제", description = "즐겨찾기에서 시설을 삭제합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Void> removeFavorite(
		@AuthenticationPrincipal User user,
		@Parameter(description = "즐겨찾기 ID", required = true)
		@PathVariable Long favoriteId
	) {
		log.info("API 호출: 즐겨찾기 삭제 - userId: {}, favoriteId: {}", user.getUserId(), favoriteId);

		facilityService.removeFavorite(user.getUserId(), favoriteId);

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 즐겨찾기 메모 수정
	 */
	@PatchMapping("/favorites/{favoriteId}/memo")
	@Operation(summary = "즐겨찾기 메모 수정", description = "즐겨찾기 시설의 메모를 수정합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Void> updateFavoriteMemo(
		@AuthenticationPrincipal User user,
		@Parameter(description = "즐겨찾기 ID", required = true)
		@PathVariable Long favoriteId,

		@RequestBody UpdateFavoriteMemoRequest request
	) {
		log.info("API 호출: 즐겨찾기 메모 수정 - userId: {}, favoriteId: {}",
			user.getUserId(), favoriteId);

		facilityService.updateFavoriteMemo(user.getUserId(), favoriteId, request.getMemo());

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 즐겨찾기 방문 기록
	 */
	@PostMapping("/favorites/{favoriteId}/visit")
	@Operation(summary = "방문 기록", description = "즐겨찾기 시설의 방문 횟수를 증가시킵니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Void> recordVisit(
		@AuthenticationPrincipal User user,
		@Parameter(description = "즐겨찾기 ID", required = true)
		@PathVariable Long favoriteId
	) {
		log.info("API 호출: 방문 기록 - userId: {}, favoriteId: {}", user.getUserId(), favoriteId);

		facilityService.recordVisit(user.getUserId(), favoriteId);

		return ApiResponse.onSuccess(null);
	}
}
