package com.rehab.domain.repository.exercise;

import com.rehab.domain.entity.UserFacilityFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 즐겨찾기 시설 Repository
 */
@Repository
public interface UserFacilityFavoriteRepository extends JpaRepository<UserFacilityFavorite, Long> {

	/**
	 * 사용자의 활성 즐겨찾기 목록 조회 (최신순)
	 */
	List<UserFacilityFavorite> findByUser_UserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);

	/**
	 * 사용자의 전체 즐겨찾기 목록 조회
	 */
	List<UserFacilityFavorite> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

	/**
	 * 특정 시설의 즐겨찾기 여부 확인
	 */
	boolean existsByUser_UserIdAndFacility_FacilityIdAndIsActiveTrue(Long userId, Long facilityId);

	/**
	 * 특정 시설의 즐겨찾기 존재 여부 (활성/비활성 모두)
	 */
	boolean existsByUser_UserIdAndFacility_FacilityId(Long userId, Long facilityId);

	/**
	 * 사용자-시설 조합으로 즐겨찾기 조회
	 */
	Optional<UserFacilityFavorite> findByUser_UserIdAndFacility_FacilityId(Long userId, Long facilityId);

	/**
	 * 사용자의 즐겨찾기 개수 조회
	 */
	Long countByUser_UserIdAndIsActiveTrue(Long userId);

	/**
	 * 시설의 즐겨찾기 개수 조회 (인기도 측정용)
	 */
	Long countByFacility_FacilityIdAndIsActiveTrue(Long facilityId);

	/**
	 * 방문 횟수가 많은 즐겨찾기 조회
	 */
	List<UserFacilityFavorite> findTop5ByUser_UserIdAndIsActiveTrueOrderByVisitCountDesc(Long userId);

	/**
	 * 사용자 ID와 즐겨찾기 ID로 조회 (권한 검증용)
	 */
	@Query("SELECT uff FROM UserFacilityFavorite uff " +
		"WHERE uff.favoriteId = :favoriteId AND uff.user.userId = :userId")
	Optional<UserFacilityFavorite> findByIdAndUserId(
		@Param("favoriteId") Long favoriteId,
		@Param("userId") Long userId
	);
}
