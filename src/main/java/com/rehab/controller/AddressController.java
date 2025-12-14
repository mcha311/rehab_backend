package com.rehab.controller;

import com.rehab.apiPayload.ApiResponse;
import com.rehab.dto.address.AddressRequest;
import com.rehab.dto.address.AddressResponse;
import com.rehab.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 주소 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Tag(name = "주소", description = "사용자 주소 관리 API")
public class AddressController {

	private final AddressService addressService;

	/**
	 * 사용자 주소 조회
	 */
	@GetMapping
	@Operation(summary = "주소 조회", description = "사용자의 등록된 주소를 조회합니다.")
	public ApiResponse<AddressResponse> getAddress(
		@Parameter(description = "사용자 ID", required = true)
		@RequestParam("userId") Long userId
	) {
		log.info("API 호출: 주소 조회 - userId: {}", userId);

		AddressResponse response = addressService.getUserAddress(userId);

		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 등록
	 */
	@PostMapping
	@Operation(summary = "주소 등록", description = "사용자의 주소를 등록합니다.")
	public ApiResponse<AddressResponse> createAddress(
		@Parameter(description = "사용자 ID", required = true)
		@RequestParam("userId") Long userId,

		@RequestBody AddressRequest request
	) {
		log.info("API 호출: 주소 등록 - userId: {}", userId);

		AddressResponse response = addressService.createAddress(userId, request);

		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 수정
	 */
	@PutMapping
	@Operation(summary = "주소 수정", description = "사용자의 주소를 수정합니다.")
	public ApiResponse<AddressResponse> updateAddress(
		@Parameter(description = "사용자 ID", required = true)
		@RequestParam("userId") Long userId,

		@RequestBody AddressRequest request
	) {
		log.info("API 호출: 주소 수정 - userId: {}", userId);

		AddressResponse response = addressService.updateAddress(userId, request);

		return ApiResponse.onSuccess(response);
	}

	/**
	 * 사용자 주소 삭제
	 */
	@DeleteMapping
	@Operation(summary = "주소 삭제", description = "사용자의 주소를 삭제합니다.")
	public ApiResponse<Void> deleteAddress(
		@Parameter(description = "사용자 ID", required = true)
		@RequestParam("userId") Long userId
	) {
		log.info("API 호출: 주소 삭제 - userId: {}", userId);

		addressService.deleteAddress(userId);

		return ApiResponse.onSuccess(null);
	}

	/**
	 * 주소 존재 여부 확인
	 */
	@GetMapping("/exists")
	@Operation(summary = "주소 존재 여부 확인", description = "사용자의 주소 등록 여부를 확인합니다.")
	public ApiResponse<Boolean> hasAddress(
		@Parameter(description = "사용자 ID", required = true)
		@RequestParam("userId") Long userId
	) {
		log.info("API 호출: 주소 존재 여부 확인 - userId: {}", userId);

		boolean exists = addressService.hasAddress(userId);

		return ApiResponse.onSuccess(exists);
	}
}
