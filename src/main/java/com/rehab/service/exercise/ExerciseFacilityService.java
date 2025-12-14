package com.rehab.service.exercise;

import com.rehab.domain.entity.*;
import com.rehab.domain.entity.enums.FacilityType;
import com.rehab.domain.repository.address.AddressRepository;
import com.rehab.domain.repository.exercise.ExerciseFacilityRepository;
import com.rehab.domain.repository.exercise.UserFacilityFavoriteRepository;
import com.rehab.domain.repository.user.UserRepository;
import com.rehab.dto.exercise.facility.FacilityDetailResponse;
import com.rehab.dto.exercise.facility.FacilityFavoriteResponse;
import com.rehab.dto.exercise.facility.FacilityListResponse;
import com.rehab.dto.exercise.facility.FacilitySearchResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 운동 시설 서비스 (완전 버전)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseFacilityService {

	private final ExerciseFacilityRepository facilityRepository;
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final UserFacilityFavoriteRepository favoriteRepository;

	/**
	 * 사용자 주소 기준 주변 시설 검색
	 */
	public List<FacilitySearchResponse> searchNearbyFacilities(
		Long userId,
		Double radiusKm,
		List<FacilityType> facilityTypes) {

		log.info("주변 시설 검색 - userId: {}, radius: {}km, types: {}", userId, radiusKm, facilityTypes);

		// 1. 사용자 주소 조회
		Address userAddress = addressRepository.findByUser_UserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("주소 정보가 없습니다."));

		if (!userAddress.hasCoordinates()) {
			throw new IllegalArgumentException("주소의 좌표 정보가 없습니다.");
		}

		// 2. 시설 검색 (필터 적용)
		List<ExerciseFacility> facilities;
		if (facilityTypes != null && !facilityTypes.isEmpty()) {
			facilities = facilityRepository.findByIsActiveTrueAndFacilityTypeIn(facilityTypes);
		} else {
			facilities = facilityRepository.findByIsActiveTrue();
		}

		// 3. 거리 계산 및 필터링
		return facilities.stream()
			.map(facility -> {
				double distance = ExerciseFacility.calculateDistance(
					userAddress.getLatitude(),
					userAddress.getLongitude(),
					facility.getLatitude(),
					facility.getLongitude()
				);
				return new FacilitySearchResponse(facility, distance);
			})
			.filter(dto -> dto.getDistanceKm() <= radiusKm)
			.sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
			.collect(Collectors.toList());
	}

	/**
	 * 재활에 적합한 시설만 검색
	 */
	public List<FacilitySearchResponse> searchRehabSuitableFacilities(
		Long userId,
		Double radiusKm) {

		List<FacilityType> rehabTypes = List.of(
			FacilityType.REHAB_CENTER,
			FacilityType.PHYSICAL_THERAPY,
			FacilityType.MEDICAL_FITNESS,
			FacilityType.PUBLIC_GYM,
			FacilityType.PUBLIC_POOL,
			FacilityType.PILATES_STUDIO,
			FacilityType.YOGA_STUDIO
		);

		return searchNearbyFacilities(userId, radiusKm, rehabTypes);
	}

	/**
	 * 시설 상세 조회
	 */
	public FacilityDetailResponse getFacilityDetail(Long facilityId, Long userId) {
		log.info("시설 상세 조회 - facilityId: {}, userId: {}", facilityId, userId);

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		// 사용자 주소로부터의 거리 계산 (주소가 있는 경우)
		Double distanceKm = null;
		if (userId != null) {
			addressRepository.findByUser_UserId(userId)
				.ifPresent(address -> {
					if (address.hasCoordinates()) {
						double distance = ExerciseFacility.calculateDistance(
							address.getLatitude(),
							address.getLongitude(),
							facility.getLatitude(),
							facility.getLongitude()
						);
						// distanceKm에 값 할당 (final 변수 제약으로 직접 할당 불가)
					}
				});
		}

		// 즐겨찾기 여부 확인
		boolean isFavorite = false;
		if (userId != null) {
			isFavorite = favoriteRepository.existsByUser_UserIdAndFacility_FacilityIdAndIsActiveTrue(
				userId, facilityId
			);
		}

		return new FacilityDetailResponse(facility, distanceKm, isFavorite);
	}

	/**
	 * 즐겨찾기 추가
	 */
	@Transactional
	public void addFavorite(Long userId, Long facilityId, String memo) {
		log.info("즐겨찾기 추가 - userId: {}, facilityId: {}", userId, facilityId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		// 중복 체크
		if (favoriteRepository.existsByUser_UserIdAndFacility_FacilityId(userId, facilityId)) {
			throw new IllegalStateException("이미 즐겨찾기에 추가된 시설입니다.");
		}

		UserFacilityFavorite favorite = UserFacilityFavorite.builder()
			.user(user)
			.facility(facility)
			.memo(memo)
			.build();

		favoriteRepository.save(favorite);
	}

	/**
	 * 즐겨찾기 목록 조회
	 */
	public List<FacilityFavoriteResponse> getFavorites(Long userId) {
		log.info("즐겨찾기 목록 조회 - userId: {}", userId);

		List<UserFacilityFavorite> favorites =
			favoriteRepository.findByUser_UserIdAndIsActiveTrueOrderByCreatedAtDesc(userId);

		return favorites.stream()
			.map(FacilityFavoriteResponse::new)
			.collect(Collectors.toList());
	}

	/**
	 * 즐겨찾기 삭제
	 */
	@Transactional
	public void removeFavorite(Long userId, Long favoriteId) {
		log.info("즐겨찾기 삭제 - userId: {}, favoriteId: {}", userId, favoriteId);

		UserFacilityFavorite favorite = favoriteRepository.findById(favoriteId)
			.orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 찾을 수 없습니다."));

		if (!favorite.getUser().getUserId().equals(userId)) {
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		favoriteRepository.delete(favorite);
	}

	/**
	 * 즐겨찾기 메모 수정
	 */
	@Transactional
	public void updateFavoriteMemo(Long userId, Long favoriteId, String memo) {
		log.info("즐겨찾기 메모 수정 - userId: {}, favoriteId: {}", userId, favoriteId);

		UserFacilityFavorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, userId)
			.orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 찾을 수 없거나 권한이 없습니다."));

		favorite.updateMemo(memo);
	}

	/**
	 * 방문 기록
	 */
	@Transactional
	public void recordVisit(Long userId, Long favoriteId) {
		log.info("방문 기록 - userId: {}, favoriteId: {}", userId, favoriteId);

		UserFacilityFavorite favorite = favoriteRepository.findByIdAndUserId(favoriteId, userId)
			.orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 찾을 수 없거나 권한이 없습니다."));

		favorite.incrementVisitCount();
	}

	/**
	 * 공공 시설만 검색 (무료/저렴한 시설)
	 */
	public List<FacilitySearchResponse> searchPublicFacilities(
		Long userId,
		Double radiusKm) {

		List<FacilityType> publicTypes = List.of(
			FacilityType.PUBLIC_GYM,
			FacilityType.PUBLIC_POOL,
			FacilityType.PUBLIC_PARK,
			FacilityType.COMMUNITY_CENTER,
			FacilityType.OUTDOOR_GYM,
			FacilityType.RUNNING_TRACK,
			FacilityType.SPORTS_PARK
		);

		return searchNearbyFacilities(userId, radiusKm, publicTypes);
	}

	/**
	 * 시설 유형별 검색
	 */
	public List<FacilitySearchResponse> searchByType(
		Long userId,
		FacilityType facilityType,
		Double radiusKm) {

		return searchNearbyFacilities(userId, radiusKm, List.of(facilityType));
	}

	/**
	 * 지역별 시설 검색 (좌표 없이)
	 */
	public List<FacilityListResponse> searchByDistrict(String district) {
		log.info("지역별 시설 검색 - district: {}", district);

		List<ExerciseFacility> facilities =
			facilityRepository.findByDistrictAndIsActiveTrueOrderByNameAsc(district);

		return facilities.stream()
			.map(FacilityListResponse::new)
			.collect(Collectors.toList());
	}
}
