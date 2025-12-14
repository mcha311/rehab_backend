package com.rehab.service.facility;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.FacilityImage;
import com.rehab.domain.repository.exercise.ExerciseFacilityRepository;
import com.rehab.domain.repository.exercise.FacilityImageRepository;
import com.rehab.dto.exercise.facility.CreateFacilityRequest;
import com.rehab.dto.exercise.facility.CreateFacilityResponse;
import com.rehab.dto.exercise.facility.UpdateFacilityRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 운동 시설 관리 서비스 (CRUD)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityManagementService {

	private final ExerciseFacilityRepository facilityRepository;
	private final FacilityImageRepository facilityImageRepository;

	/**
	 * 운동 시설 생성
	 */
	@Transactional
	public CreateFacilityResponse createFacility(CreateFacilityRequest request) {
		log.info("운동 시설 생성 - name: {}, type: {}", request.getName(), request.getFacilityType());

		// 중복 체크 (외부 ID가 있는 경우)
		if (request.getExternalId() != null && request.getExternalSource() != null) {
			facilityRepository.findByExternalSourceAndExternalId(
				request.getExternalSource(),
				request.getExternalId()
			).ifPresent(facility -> {
				throw new IllegalStateException("이미 등록된 시설입니다. (externalId: " + request.getExternalId() + ")");
			});
		}

		// ExerciseFacility 엔티티 생성
		ExerciseFacility facility = ExerciseFacility.builder()
			.name(request.getName())
			.facilityType(request.getFacilityType())
			.description(request.getDescription())
			.city(request.getCity())
			.district(request.getDistrict())
			.street(request.getStreet())
			.specAddress(request.getSpecAddress())
			.zipcode(request.getZipcode())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.phoneNumber(request.getPhoneNumber())
			.websiteUrl(request.getWebsiteUrl())
			.operatingHours(request.getOperatingHours())
			.closedDays(request.getClosedDays())
			.parkingAvailable(request.getParkingAvailable())
			.wheelchairAccessible(request.getWheelchairAccessible())
			.showerAvailable(request.getShowerAvailable())
			.lockerAvailable(request.getLockerAvailable())
			.facilities(request.getFacilities())
			.amenities(request.getAmenities())
			.externalId(request.getExternalId())
			.externalSource(request.getExternalSource())
			.rating(request.getRating())
			.reviewCount(request.getReviewCount())
			.entryFee(request.getEntryFee())
			.monthlyFee(request.getMonthlyFee())
			.pricingInfo(request.getPricingInfo())
			.isActive(true)
			.isVerified(false)  // 기본값: 미검증
			.build();

		ExerciseFacility savedFacility = facilityRepository.save(facility);

		// 이미지 처리 (있는 경우)
		if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
			saveImages(savedFacility, request.getImageUrls(), request.getMainImageIndex());
		}

		log.info("운동 시설 생성 완료 - facilityId: {}", savedFacility.getFacilityId());

		return CreateFacilityResponse.from(savedFacility);
	}

	/**
	 * 운동 시설 수정
	 */
	@Transactional
	public CreateFacilityResponse updateFacility(Long facilityId, UpdateFacilityRequest request) {
		log.info("운동 시설 수정 - facilityId: {}", facilityId);

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		// 기본 정보 업데이트
		if (request.getName() != null || request.getFacilityType() != null || request.getDescription() != null) {
			facility.updateBasicInfo(
				request.getName() != null ? request.getName() : facility.getName(),
				request.getFacilityType() != null ? request.getFacilityType() : facility.getFacilityType(),
				request.getDescription() != null ? request.getDescription() : facility.getDescription()
			);
		}

		// 운영 정보 업데이트
		if (request.getOperatingHours() != null || request.getClosedDays() != null || request.getPhoneNumber() != null) {
			facility.updateOperationInfo(
				request.getOperatingHours() != null ? request.getOperatingHours() : facility.getOperatingHours(),
				request.getClosedDays() != null ? request.getClosedDays() : facility.getClosedDays(),
				request.getPhoneNumber() != null ? request.getPhoneNumber() : facility.getPhoneNumber()
			);
		}

		// 평점 정보 업데이트
		if (request.getRating() != null || request.getReviewCount() != null) {
			facility.updateRating(
				request.getRating() != null ? request.getRating() : facility.getRating(),
				request.getReviewCount() != null ? request.getReviewCount() : facility.getReviewCount()
			);
		}

		log.info("운동 시설 수정 완료 - facilityId: {}", facilityId);

		return CreateFacilityResponse.from(facility);
	}

	/**
	 * 운동 시설 삭제 (비활성화)
	 */
	@Transactional
	public void deleteFacility(Long facilityId) {
		log.info("운동 시설 삭제 - facilityId: {}", facilityId);

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		facility.deactivate();

		log.info("운동 시설 삭제 완료 (비활성화) - facilityId: {}", facilityId);
	}

	/**
	 * 운동 시설 검증 완료 처리
	 */
	@Transactional
	public void verifyFacility(Long facilityId) {
		log.info("운동 시설 검증 - facilityId: {}", facilityId);

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		facility.verify();

		log.info("운동 시설 검증 완료 - facilityId: {}", facilityId);
	}

	/**
	 * 운동 시설 활성화
	 */
	@Transactional
	public void activateFacility(Long facilityId) {
		log.info("운동 시설 활성화 - facilityId: {}", facilityId);

		ExerciseFacility facility = facilityRepository.findById(facilityId)
			.orElseThrow(() -> new IllegalArgumentException("시설을 찾을 수 없습니다."));

		facility.activate();

		log.info("운동 시설 활성화 완료 - facilityId: {}", facilityId);
	}

	/**
	 * 이미지 저장
	 */
	private void saveImages(ExerciseFacility facility, List<String> imageUrls, Integer mainImageIndex) {
		List<FacilityImage> images = new ArrayList<>();

		for (int i = 0; i < imageUrls.size(); i++) {
			boolean isMain = (mainImageIndex != null && mainImageIndex == i);

			FacilityImage image = FacilityImage.builder()
				.facility(facility)
				.imageUrl(List.of(imageUrls.get(i)))
				.isMain(isMain)
				.displayOrder(i)
				.build();

			images.add(image);
		}

		facilityImageRepository.saveAll(images);
		log.info("시설 이미지 저장 완료 - {} 개", images.size());
	}

	/**
	 * 대량 시설 생성 (배치용)
	 */
	@Transactional
	public List<CreateFacilityResponse> createFacilitiesBatch(List<CreateFacilityRequest> requests) {
		log.info("대량 시설 생성 - count: {}", requests.size());

		List<CreateFacilityResponse> responses = new ArrayList<>();

		for (CreateFacilityRequest request : requests) {
			try {
				CreateFacilityResponse response = createFacility(request);
				responses.add(response);
			} catch (Exception e) {
				log.error("시설 생성 실패 - name: {}, error: {}", request.getName(), e.getMessage());
				// 실패해도 계속 진행
			}
		}

		log.info("대량 시설 생성 완료 - 성공: {}/{}", responses.size(), requests.size());

		return responses;
	}
}
