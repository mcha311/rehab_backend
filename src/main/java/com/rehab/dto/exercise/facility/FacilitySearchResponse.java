package com.rehab.dto.exercise.facility;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.FacilityImage;
import com.rehab.domain.entity.enums.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시설 검색 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "운동 시설 검색 결과")
public class FacilitySearchResponse {

	@Schema(description = "시설 ID")
	private Long facilityId;

	@Schema(description = "시설명")
	private String name;

	@Schema(description = "시설 유형")
	private FacilityType facilityType;

	@Schema(description = "시설 유형 표시명")
	private String facilityTypeDisplayName;

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

	@Schema(description = "운영시간")
	private String operatingHours;

	@Schema(description = "입장료 (원)")
	private Integer entryFee;

	@Schema(description = "월 이용료 (원)")
	private Integer monthlyFee;

	@Schema(description = "주차 가능")
	private Boolean parkingAvailable;

	@Schema(description = "휠체어 접근 가능")
	private Boolean wheelchairAccessible;

	@Schema(description = "평점")
	private Double rating;

	@Schema(description = "대표 이미지 URL")
	private String mainImageUrl;

	public FacilitySearchResponse(ExerciseFacility facility, Double distanceKm) {
		this.facilityId = facility.getFacilityId();
		this.name = facility.getName();
		this.facilityType = facility.getFacilityType();
		this.facilityTypeDisplayName = facility.getFacilityType().getDisplayName();
		this.address = facility.getFullAddress();
		this.distanceKm = Math.round(distanceKm * 100.0) / 100.0; // 소수점 2자리
		this.latitude = facility.getLatitude();
		this.longitude = facility.getLongitude();
		this.phoneNumber = facility.getPhoneNumber();
		this.operatingHours = facility.getOperatingHours();
		this.entryFee = facility.getEntryFee();
		this.monthlyFee = facility.getMonthlyFee();
		this.parkingAvailable = facility.getParkingAvailable();
		this.wheelchairAccessible = facility.getWheelchairAccessible();
		this.rating = facility.getRating();

		// 대표 이미지 추출
		this.mainImageUrl = facility.getImages().stream()
			.filter(FacilityImage::getIsMain)
			.findFirst()
			.map(img -> img.getImageUrl() != null && !img.getImageUrl().isEmpty()
				? img.getImageUrl().get(0)
				: null)
			.orElse(null);
	}
}

