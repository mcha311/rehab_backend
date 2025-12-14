package com.rehab.dto.exercise.facility;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.enums.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시설 생성 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "시설 생성 응답")
public class CreateFacilityResponse {

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

	@Schema(description = "위도")
	private Double latitude;

	@Schema(description = "경도")
	private Double longitude;

	@Schema(description = "전화번호")
	private String phoneNumber;

	@Schema(description = "활성 상태")
	private Boolean isActive;

	@Schema(description = "검증 완료 여부")
	private Boolean isVerified;

	@Schema(description = "생성일시")
	private LocalDateTime createdAt;

	public static CreateFacilityResponse from(ExerciseFacility facility) {
		return CreateFacilityResponse.builder()
			.facilityId(facility.getFacilityId())
			.name(facility.getName())
			.facilityType(facility.getFacilityType())
			.facilityTypeDisplayName(facility.getFacilityType().getDisplayName())
			.address(facility.getFullAddress())
			.latitude(facility.getLatitude())
			.longitude(facility.getLongitude())
			.phoneNumber(facility.getPhoneNumber())
			.isActive(facility.getIsActive())
			.isVerified(facility.getIsVerified())
			.createdAt(facility.getCreatedAt())
			.build();
	}
}
