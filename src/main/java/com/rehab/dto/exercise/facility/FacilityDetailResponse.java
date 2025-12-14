package com.rehab.dto.exercise.facility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.enums.FacilityType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시설 상세 정보 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "운동 시설 상세 정보")
public class FacilityDetailResponse {

	@Schema(description = "시설 ID")
	private Long facilityId;

	@Schema(description = "시설명")
	private String name;

	@Schema(description = "시설 유형")
	private FacilityType facilityType;

	@Schema(description = "시설 유형 표시명")
	private String facilityTypeDisplayName;

	@Schema(description = "시설 설명")
	private String description;

	@Schema(description = "주소")
	private String address;

	@Schema(description = "거리 (km)")
	private Double distanceKm;

	@Schema(description = "위도")
	private Double latitude;

	@Schema(description = "경도")
	private Double longitude;

	@Schema(description = "전화번호")
	private String phoneNumber;

	@Schema(description = "웹사이트 URL")
	private String websiteUrl;

	@Schema(description = "운영시간")
	private String operatingHours;

	@Schema(description = "휴무일")
	private String closedDays;

	@Schema(description = "주차 가능")
	private Boolean parkingAvailable;

	@Schema(description = "휠체어 접근 가능")
	private Boolean wheelchairAccessible;

	@Schema(description = "샤워실")
	private Boolean showerAvailable;

	@Schema(description = "락커")
	private Boolean lockerAvailable;

	@Schema(description = "시설 정보")
	private Map<String, Object> facilities;

	@Schema(description = "편의시설")
	private Map<String, Object> amenities;

	@Schema(description = "입장료 (원)")
	private Integer entryFee;

	@Schema(description = "월 이용료 (원)")
	private Integer monthlyFee;

	@Schema(description = "가격 정보")
	private Map<String, Object> pricingInfo;

	@Schema(description = "평점")
	private Double rating;

	@Schema(description = "리뷰 개수")
	private Integer reviewCount;

	@Schema(description = "이미지 목록")
	private List<FacilityImageDto> images;

	@Schema(description = "즐겨찾기 여부")
	private Boolean isFavorite;

	@Schema(description = "재활 적합 여부")
	private Boolean isSuitableForRehab;

	@Schema(description = "공공 시설 여부")
	private Boolean isPublicFacility;

	public FacilityDetailResponse(ExerciseFacility facility, Double distanceKm, Boolean isFavorite) {
		this.facilityId = facility.getFacilityId();
		this.name = facility.getName();
		this.facilityType = facility.getFacilityType();
		this.facilityTypeDisplayName = facility.getFacilityType().getDisplayName();
		this.description = facility.getDescription();
		this.address = facility.getFullAddress();
		this.distanceKm = distanceKm != null ? Math.round(distanceKm * 100.0) / 100.0 : null;
		this.latitude = facility.getLatitude();
		this.longitude = facility.getLongitude();
		this.phoneNumber = facility.getPhoneNumber();
		this.websiteUrl = facility.getWebsiteUrl();
		this.operatingHours = facility.getOperatingHours();
		this.closedDays = facility.getClosedDays();
		this.parkingAvailable = facility.getParkingAvailable();
		this.wheelchairAccessible = facility.getWheelchairAccessible();
		this.showerAvailable = facility.getShowerAvailable();
		this.lockerAvailable = facility.getLockerAvailable();
		this.facilities = facility.getFacilities();
		this.amenities = facility.getAmenities();
		this.entryFee = facility.getEntryFee();
		this.monthlyFee = facility.getMonthlyFee();
		this.pricingInfo = facility.getPricingInfo();
		this.rating = facility.getRating();
		this.reviewCount = facility.getReviewCount();
		this.images = facility.getImages().stream()
			.map(FacilityImageDto::new)
			.collect(Collectors.toList());
		this.isFavorite = isFavorite;
		this.isSuitableForRehab = facility.getFacilityType().isSuitableForRehab();
		this.isPublicFacility = facility.getFacilityType().isPublicFacility();
	}
}
