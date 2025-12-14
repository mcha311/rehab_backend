package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 주소 엔티티
 * User와 1:1 관계
 */
@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long addressId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(name = "city")
	private String city;  // 시/도 (예: 서울특별시, 경기도)

	@Column(name = "district")
	private String district;  // 시/군/구 (예: 강남구, 수원시)

	@Column(name = "street")
	private String street;  // 도로명 (예: 테헤란로)

	@Column(name = "zipcode")
	private String zipcode;  // 우편번호

	@Column(name = "street_code")
	private String streetCode;  // 도로명 코드 (행정안전부 표준)

	@Column(name = "spec_address")
	private String specAddress;  // 상세 주소 (예: 101동 101호)

	@Column(name = "latitude")
	private Double latitude;  // 위도 (추가)

	@Column(name = "longitude")
	private Double longitude;  // 경도 (추가)

	// ================================
	// 연관관계 편의 메서드
	// ================================

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 주소 정보 업데이트
	 */
	public void updateAddress(String city, String district, String street,
		String zipcode, String streetCode, String specAddress) {
		this.city = city;
		this.district = district;
		this.street = street;
		this.zipcode = zipcode;
		this.streetCode = streetCode;
		this.specAddress = specAddress;
	}

	/**
	 * 좌표 정보 업데이트 (Geocoding API 사용 시)
	 */
	public void updateCoordinates(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * 전체 주소 문자열 반환
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
	 * 좌표가 설정되어 있는지 확인
	 */
	public boolean hasCoordinates() {
		return latitude != null && longitude != null;
	}
}
