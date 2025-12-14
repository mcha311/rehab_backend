package com.rehab.dto.exercise.facility;

import com.rehab.domain.entity.enums.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 운동 시설 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "운동 시설 수정 요청")
public class UpdateFacilityRequest {

	@Schema(description = "시설명", example = "강남구민체육센터")
	private String name;

	@Schema(description = "시설 유형", example = "PUBLIC_GYM")
	private FacilityType facilityType;

	@Schema(description = "시설 설명", example = "강남구에서 운영하는 공공 체육센터입니다.")
	private String description;

	@Schema(description = "전화번호", example = "02-1234-5678")
	private String phoneNumber;

	@Schema(description = "웹사이트 URL", example = "https://www.facility.com")
	private String websiteUrl;

	@Schema(description = "운영시간", example = "평일 06:00-22:00, 주말 08:00-20:00")
	private String operatingHours;

	@Schema(description = "휴무일", example = "매월 첫째주 월요일")
	private String closedDays;

	@Schema(description = "주차 가능 여부", example = "true")
	private Boolean parkingAvailable;

	@Schema(description = "휠체어 접근 가능 여부", example = "true")
	private Boolean wheelchairAccessible;

	@Schema(description = "샤워실 여부", example = "true")
	private Boolean showerAvailable;

	@Schema(description = "락커 여부", example = "true")
	private Boolean lockerAvailable;

	@Schema(description = "입장료 (원)", example = "3000")
	private Integer entryFee;

	@Schema(description = "월 이용료 (원)", example = "50000")
	private Integer monthlyFee;

	@Schema(description = "평점", example = "4.5")
	private Double rating;

	@Schema(description = "리뷰 개수", example = "128")
	private Integer reviewCount;
}
