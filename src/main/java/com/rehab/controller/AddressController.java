package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.domain.entity.User;
import com.rehab.dto.address.AddressRequest;
import com.rehab.dto.address.AddressResponse;
import com.rehab.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 주소 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Tag(name = "주소", description = "사용자 주소 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

	private final AddressService addressService;

	/**
	 * 사용자 주소 조회
	 */
	@GetMapping
	@Operation(summary = "주소 조회", description = "사용자의 등록된 주소를 조회합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<AddressResponse> getAddress(
		@AuthenticationPrincipal User user
	) {
		log.info("API 호출: 주소 조회 - userId: {}", user.getUserId());

		AddressResponse response = addressService.getUserAddress(user.getUserId());
		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 등록
	 */
	@PostMapping
	@Operation(summary = "주소 등록", description = "사용자의 주소를 등록합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<AddressResponse> createAddress(
		@AuthenticationPrincipal User user,
		@RequestBody AddressRequest request
	) {
		log.info("API 호출: 주소 등록 - userId: {}", user.getUserId());

		AddressResponse response = addressService.createAddress(user.getUserId(), request);
		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 수정
	 */
	@PutMapping
	@Operation(summary = "주소 수정", description = "사용자의 주소를 수정합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<AddressResponse> updateAddress(
		@AuthenticationPrincipal User user,
		@RequestBody AddressRequest request
	) {
		log.info("API 호출: 주소 수정 - userId: {}", user.getUserId());

		AddressResponse response = addressService.updateAddress(user.getUserId(), request);
		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 삭제
	 */
	@DeleteMapping
	@Operation(summary = "주소 삭제", description = "사용자의 주소를 삭제합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Void> deleteAddress(
		@AuthenticationPrincipal User user
	) {
		log.info("API 호출: 주소 삭제 - userId: {}", user.getUserId());

		addressService.deleteAddress(user.getUserId());
		return ApiResponse.onSuccess(null);
	}

	/**
	 * 주소 존재 여부 확인
	 */
	@GetMapping("/exists")
	@Operation(summary = "주소 존재 여부 확인", description = "사용자의 주소 등록 여부를 확인합니다. 인증된 사용자 정보를 자동으로 추출합니다.")
	public ApiResponse<Boolean> hasAddress(
		@AuthenticationPrincipal User user
	) {
		log.info("API 호출: 주소 존재 여부 확인 - userId: {}", user.getUserId());

		boolean exists = addressService.hasAddress(user.getUserId());
		return ApiResponse.onSuccess(exists);
	}
}
