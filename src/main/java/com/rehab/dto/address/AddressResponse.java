package com.rehab.dto.address;

import java.time.LocalDateTime;

import com.rehab.domain.entity.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주소 정보")
public class AddressResponse {

	@Schema(description = "주소 ID")
	private Long addressId;

	@Schema(description = "사용자 ID")
	private Long userId;

	@Schema(description = "시/도")
	private String city;

	@Schema(description = "시/군/구")
	private String district;

	@Schema(description = "도로명")
	private String street;

	@Schema(description = "우편번호")
	private String zipcode;

	@Schema(description = "도로명코드")
	private String streetCode;

	@Schema(description = "상세주소")
	private String specAddress;

	@Schema(description = "전체 주소")
	private String fullAddress;

	@Schema(description = "위도")
	private Double latitude;

	@Schema(description = "경도")
	private Double longitude;

	@Schema(description = "좌표 보유 여부")
	private Boolean hasCoordinates;

	@Schema(description = "생성일시")
	private LocalDateTime createdAt;

	@Schema(description = "수정일시")
	private LocalDateTime updatedAt;

	public static AddressResponse from(Address address) {
		return AddressResponse.builder()
			.addressId(address.getAddressId())
			.userId(address.getUser().getUserId())
			.city(address.getCity())
			.district(address.getDistrict())
			.street(address.getStreet())
			.zipcode(address.getZipcode())
			.streetCode(address.getStreetCode())
			.specAddress(address.getSpecAddress())
			.fullAddress(address.getFullAddress())
			.latitude(address.getLatitude())
			.longitude(address.getLongitude())
			.hasCoordinates(address.hasCoordinates())
			.createdAt(address.getCreatedAt())
			.updatedAt(address.getUpdatedAt())
			.build();
	}
}
