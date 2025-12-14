package com.rehab.dto.address;

import com.rehab.domain.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주소 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주소 등록/수정 요청")
public class AddressRequest {

	@Schema(description = "시/도", example = "서울특별시")
	private String city;

	@Schema(description = "시/군/구", example = "강남구")
	private String district;

	@Schema(description = "도로명", example = "테헤란로")
	private String street;

	@Schema(description = "우편번호", example = "06234")
	private String zipcode;

	@Schema(description = "도로명코드", example = "1234567890")
	private String streetCode;

	@Schema(description = "상세주소", example = "101동 101호")
	private String specAddress;

	@Schema(description = "위도", example = "37.4979")
	private Double latitude;

	@Schema(description = "경도", example = "127.0276")
	private Double longitude;
}








