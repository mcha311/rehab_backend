package com.rehab.domain.repository.exercise;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.enums.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 운동 시설 Repository
 */
@Repository
public interface ExerciseFacilityRepository extends JpaRepository<ExerciseFacility, Long> {

	/**
	 * 활성 시설 전체 조회
	 */
	List<ExerciseFacility> findByIsActiveTrue();

	/**
	 * 시설 유형으로 조회 (활성 시설만)
	 */
	List<ExerciseFacility> findByIsActiveTrueAndFacilityType(FacilityType facilityType);

	/**
	 * 여러 시설 유형으로 조회 (활성 시설만)
	 */
	List<ExerciseFacility> findByIsActiveTrueAndFacilityTypeIn(List<FacilityType> facilityTypes);

	/**
	 * 지역별 조회 (활성 시설만)
	 */
	List<ExerciseFacility> findByDistrictAndIsActiveTrueOrderByNameAsc(String district);

	/**
	 * 시/도, 시/군/구로 조회
	 */
	List<ExerciseFacility> findByCityAndDistrictAndIsActiveTrue(String city, String district);

	/**
	 * 외부 ID로 조회 (중복 체크용)
	 */
	Optional<ExerciseFacility> findByExternalSourceAndExternalId(String externalSource, String externalId);

	/**
	 * 시설명으로 검색 (LIKE)
	 */
	List<ExerciseFacility> findByNameContainingAndIsActiveTrue(String name);

	/**
	 * 특정 좌표 기준 반경 내 시설 조회 (Native Query)
	 * Haversine 공식 사용
	 */
	@Query(value = """
        SELECT f.*,
               (6371 * ACOS(
                   COS(RADIANS(:latitude)) * COS(RADIANS(f.latitude)) *
                   COS(RADIANS(f.longitude) - RADIANS(:longitude)) +
                   SIN(RADIANS(:latitude)) * SIN(RADIANS(f.latitude))
               )) AS distance
        FROM exercise_facility f
        WHERE f.is_active = TRUE
        HAVING distance <= :radiusKm
        ORDER BY distance
        LIMIT :limit
        """, nativeQuery = true)
	List<Object[]> findNearbyFacilities(
		@Param("latitude") Double latitude,
		@Param("longitude") Double longitude,
		@Param("radiusKm") Double radiusKm,
		@Param("limit") Integer limit
	);

	/**
	 * 재활에 적합한 시설만 조회
	 */
	@Query("SELECT f FROM ExerciseFacility f WHERE f.isActive = true " +
		"AND f.facilityType IN ('REHAB_CENTER', 'PHYSICAL_THERAPY', 'MEDICAL_FITNESS', " +
		"'PUBLIC_GYM', 'PUBLIC_POOL', 'PILATES_STUDIO', 'YOGA_STUDIO')")
	List<ExerciseFacility> findRehabSuitableFacilities();

	/**
	 * 공공 시설만 조회
	 */
	@Query("SELECT f FROM ExerciseFacility f WHERE f.isActive = true " +
		"AND f.facilityType IN ('PUBLIC_GYM', 'PUBLIC_POOL', 'PUBLIC_PARK', " +
		"'COMMUNITY_CENTER', 'OUTDOOR_GYM', 'RUNNING_TRACK', 'SPORTS_PARK')")
	List<ExerciseFacility> findPublicFacilities();

	/**
	 * 검증된 시설만 조회
	 */
	List<ExerciseFacility> findByIsActiveTrueAndIsVerifiedTrue();

	/**
	 * 평점 높은 순으로 조회
	 */
	List<ExerciseFacility> findTop10ByIsActiveTrueOrderByRatingDesc();

	/**
	 * 시설 개수 조회 (통계용)
	 */
	Long countByIsActiveTrue();

	/**
	 * 시설 유형별 개수 조회
	 */
	Long countByFacilityTypeAndIsActiveTrue(FacilityType facilityType);

	/**
	 * 지역별 시설 개수 조회
	 */
	Long countByDistrictAndIsActiveTrue(String district);
}
