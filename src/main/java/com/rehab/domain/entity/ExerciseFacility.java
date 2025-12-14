package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.FacilityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 운동 시설 마스터 엔티티
 * 공공데이터 포털 API 또는 카카오/네이버 지도 API에서 수집
 */
@Entity
@Table(name = "exercise_facility", indexes = {
	@Index(name = "idx_facility_type", columnList = "facility_type"),
	@Index(name = "idx_facility_location", columnList = "latitude, longitude"),
	@Index(name = "idx_facility_district", columnList = "district")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExerciseFacility extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_id")
	private Long facilityId;

	@Column(name = "name", nullable = false)
	private String name;  // 시설명

	@Enumerated(EnumType.STRING)
	@Column(name = "facility_type", nullable = false)
	private FacilityType facilityType;  // 시설 유형

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;  // 시설 설명

	// ================================
	// 주소 정보
	// ================================

	@Column(name = "city")
	private String city;  // 시/도

	@Column(name = "district")
	private String district;  // 시/군/구

	@Column(name = "street")
	private String street;  // 도로명

	@Column(name = "spec_address")
	private String specAddress;  // 상세주소

	@Column(name = "zipcode")
	private String zipcode;  // 우편번호

	// ================================
	// 위치 정보 (필수)
	// ================================

	@Column(name = "latitude", nullable = false)
	private Double latitude;  // 위도

	@Column(name = "longitude", nullable = false)
	private Double longitude;  // 경도

	// ================================
	// 연락처 및 운영 정보
	// ================================

	@Column(name = "phone_number")
	private String phoneNumber;  // 전화번호

	@Column(name = "website_url")
	private String websiteUrl;  // 웹사이트 URL

	@Column(name = "operating_hours")
	private String operatingHours;  // 운영 시간

	@Column(name = "closed_days")
	private String closedDays;  // 휴무일

	// ================================
	// 부가 정보
	// ================================

	@Column(name = "parking_available")
	private Boolean parkingAvailable;  // 주차 가능 여부

	@Column(name = "wheelchair_accessible")
	private Boolean wheelchairAccessible;  // 휠체어 접근 가능 여부

	@Column(name = "shower_available")
	private Boolean showerAvailable;  // 샤워실 여부

	@Column(name = "locker_available")
	private Boolean lockerAvailable;  // 락커 여부

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "facilities", columnDefinition = "json")
	private Map<String, Object> facilities;  // 시설 정보 (JSON: {"pool": true, "sauna": true})

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "amenities", columnDefinition = "json")
	private Map<String, Object> amenities;  // 편의시설 (JSON)

	// ================================
	// 외부 API 연동 정보
	// ================================

	@Column(name = "external_id")
	private String externalId;  // 외부 API ID (카카오/네이버 Place ID)

	@Column(name = "external_source")
	private String externalSource;  // 외부 소스 (KAKAO, NAVER, PUBLIC_DATA)

	@Column(name = "rating")
	private Double rating;  // 평점 (외부 API에서 가져온 평점)

	@Column(name = "review_count")
	private Integer reviewCount;  // 리뷰 개수

	// ================================
	// 가격 정보
	// ================================

	@Column(name = "entry_fee")
	private Integer entryFee;  // 입장료 (원)

	@Column(name = "monthly_fee")
	private Integer monthlyFee;  // 월 이용료 (원)

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "pricing_info", columnDefinition = "json")
	private Map<String, Object> pricingInfo;  // 가격 정보 (JSON)

	// ================================
	// 상태 관리
	// ================================

	@Column(name = "is_active")
	@Builder.Default
	private Boolean isActive = true;  // 활성 상태

	@Column(name = "is_verified")
	@Builder.Default
	private Boolean isVerified = false;  // 검증 완료 여부

	// ================================
	// 연관관계
	// ================================

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<FacilityImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<UserFacilityFavorite> favorites = new ArrayList<>();

	// ================================
	// 비즈니스 로직
	// ================================

	/**
	 * 전체 주소 반환
	 */
	public String getFullAddress() {
		StringBuilder sb = new StringBuilder();
		if (city != null) sb.append(city).append(" ");
		if (district != null) sb.append(district).append(" ");
		if (street != null) sb.append(street).append(" ");
		if (specAddress != null) sb.append(specAddress);
		return sb.toString().trim();
	}

	/**
	 * 두 좌표 간의 거리 계산 (Haversine 공식, 단위: km)
	 */
	public static double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
		final int EARTH_RADIUS_KM = 6371;

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS_KM * c;
	}

	/**
	 * 사용자 주소로부터의 거리 계산
	 */
	public double getDistanceFrom(Address userAddress) {
		if (userAddress == null || !userAddress.hasCoordinates()) {
			return Double.MAX_VALUE;
		}
		return calculateDistance(
			userAddress.getLatitude(),
			userAddress.getLongitude(),
			this.latitude,
			this.longitude
		);
	}

	/**
	 * 시설 이미지 추가
	 */
	public void addImage(FacilityImage image) {
		this.images.add(image);
		if (image.getFacility() != this) {
			image.setFacility(this);
		}
	}

	/**
	 * 시설 정보 업데이트
	 */
	public void updateBasicInfo(String name, FacilityType facilityType, String description) {
		this.name = name;
		this.facilityType = facilityType;
		this.description = description;
	}

	/**
	 * 운영 정보 업데이트
	 */
	public void updateOperationInfo(String operatingHours, String closedDays, String phoneNumber) {
		this.operatingHours = operatingHours;
		this.closedDays = closedDays;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 외부 평점 정보 업데이트
	 */
	public void updateRating(Double rating, Integer reviewCount) {
		this.rating = rating;
		this.reviewCount = reviewCount;
	}

	/**
	 * 시설 활성화/비활성화
	 */
	public void activate() {
		this.isActive = true;
	}

	public void deactivate() {
		this.isActive = false;
	}

	/**
	 * 검증 완료 처리
	 */
	public void verify() {
		this.isVerified = true;
	}
}







