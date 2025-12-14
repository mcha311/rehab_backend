package com.rehab.dto.exercise.facility;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.enums.FacilityType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시설 목록 응답 DTO (간단 버전)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "운동 시설 목록")
public class FacilityListResponse {

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

	@Schema(description = "전화번호")
	private String phoneNumber;

	@Schema(description = "평점")
	private Double rating;

	public FacilityListResponse(ExerciseFacility facility) {
		this.facilityId = facility.getFacilityId();
		this.name = facility.getName();
		this.facilityType = facility.getFacilityType();
		this.facilityTypeDisplayName = facility.getFacilityType().getDisplayName();
		this.address = facility.getFullAddress();
		this.phoneNumber = facility.getPhoneNumber();
		this.rating = facility.getRating();
	}
}
