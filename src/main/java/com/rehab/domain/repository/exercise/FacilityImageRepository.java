package com.rehab.domain.repository.exercise;

import com.rehab.domain.entity.FacilityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 시설 이미지 Repository
 */
@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {

	/**
	 * 시설별 이미지 목록 조회
	 */
	List<FacilityImage> findByFacility_FacilityIdOrderByDisplayOrderAsc(Long facilityId);

	/**
	 * 시설의 대표 이미지 조회
	 */
	Optional<FacilityImage> findByFacility_FacilityIdAndIsMainTrue(Long facilityId);

	/**
	 * 시설별 이미지 개수 조회
	 */
	Long countByFacility_FacilityId(Long facilityId);

	/**
	 * 시설의 모든 이미지 삭제
	 */
	void deleteByFacility_FacilityId(Long facilityId);
}
