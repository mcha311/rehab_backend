package com.rehab.dto.exercise.facility;

import com.rehab.domain.entity.enums.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 운동 시설 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "운동 시설 생성 요청")
public class CreateFacilityRequest {

	@NotBlank(message = "시설명은 필수입니다.")
	@Schema(description = "시설명", required = true, example = "강남구민체육센터")
	private String name;

	@NotNull(message = "시설 유형은 필수입니다.")
	@Schema(description = "시설 유형", required = true, example = "PUBLIC_GYM")
	private FacilityType facilityType;

	@Schema(description = "시설 설명", example = "강남구에서 운영하는 공공 체육센터입니다.")
	private String description;

	// ================================
	// 주소 정보
	// ================================

	@Schema(description = "시/도", example = "서울특별시")
	private String city;

	@Schema(description = "시/군/구", example = "강남구")
	private String district;

	@Schema(description = "도로명", example = "테헤란로")
	private String street;

	@Schema(description = "상세주소", example = "123")
	private String specAddress;

	@Schema(description = "우편번호", example = "06234")
	private String zipcode;

	// ================================
	// 위치 정보 (필수)
	// ================================

	@NotNull(message = "위도는 필수입니다.")
	@Schema(description = "위도", required = true, example = "37.4979")
	private Double latitude;

	@NotNull(message = "경도는 필수입니다.")
	@Schema(description = "경도", required = true, example = "127.0276")
	private Double longitude;

	// ================================
	// 연락처 및 운영 정보
	// ================================

	@Schema(description = "전화번호", example = "02-1234-5678")
	private String phoneNumber;

	@Schema(description = "웹사이트 URL", example = "https://www.facility.com")
	private String websiteUrl;

	@Schema(description = "운영시간", example = "평일 06:00-22:00, 주말 08:00-20:00")
	private String operatingHours;

	@Schema(description = "휴무일", example = "매월 첫째주 월요일")
	private String closedDays;

	// ================================
	// 부가 정보
	// ================================

	@Schema(description = "주차 가능 여부", example = "true")
	private Boolean parkingAvailable;

	@Schema(description = "휠체어 접근 가능 여부", example = "true")
	private Boolean wheelchairAccessible;

	@Schema(description = "샤워실 여부", example = "true")
	private Boolean showerAvailable;

	@Schema(description = "락커 여부", example = "true")
	private Boolean lockerAvailable;

	@Schema(description = "시설 정보 (JSON)", example = "{\"pool\": true, \"sauna\": false}")
	private Map<String, Object> facilities;

	@Schema(description = "편의시설 (JSON)", example = "{\"wifi\": true, \"cafe\": true}")
	private Map<String, Object> amenities;

	// ================================
	// 외부 API 연동 정보
	// ================================

	@Schema(description = "외부 API ID", example = "kakao_12345")
	private String externalId;

	@Schema(description = "외부 소스", example = "KAKAO")
	private String externalSource;

	@Schema(description = "평점", example = "4.5")
	private Double rating;

	@Schema(description = "리뷰 개수", example = "128")
	private Integer reviewCount;

	// ================================
	// 가격 정보
	// ================================

	@Schema(description = "입장료 (원)", example = "3000")
	private Integer entryFee;

	@Schema(description = "월 이용료 (원)", example = "50000")
	private Integer monthlyFee;

	@Schema(description = "가격 정보 (JSON)", example = "{\"1day\": 5000, \"1month\": 50000}")
	private Map<String, Object> pricingInfo;

	// ================================
	// 이미지 정보
	// ================================

	@Schema(description = "이미지 URL 목록", example = "[\"https://image1.jpg\", \"https://image2.jpg\"]")
	private List<String> imageUrls;

	@Schema(description = "대표 이미지 인덱스 (0부터 시작)", example = "0")
	private Integer mainImageIndex;
}







