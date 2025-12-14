package com.rehab.service.address;

import com.rehab.domain.entity.Address;
import com.rehab.domain.entity.User;
import com.rehab.domain.repository.address.AddressRepository;
import com.rehab.domain.repository.user.UserRepository;
import com.rehab.dto.address.AddressRequest;
import com.rehab.dto.address.AddressResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주소 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	// private final GeocodingService geocodingService; // 좌표 변환 서비스 (선택적)

	/**
	 * 사용자 주소 조회
	 */
	public AddressResponse getUserAddress(Long userId) {
		log.info("사용자 주소 조회 - userId: {}", userId);

		Address address = addressRepository.findByUser_UserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

		return AddressResponse.from(address);
	}

	/**
	 * 사용자 주소 등록
	 */
	@Transactional
	public AddressResponse createAddress(Long userId, AddressRequest request) {
		log.info("사용자 주소 등록 - userId: {}", userId);

		// 기존 주소 존재 여부 확인
		if (addressRepository.existsByUser_UserId(userId)) {
			throw new IllegalStateException("이미 등록된 주소가 있습니다. 수정을 이용해주세요.");
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		// Address 엔티티 생성
		Address address = Address.builder()
			.user(user)
			.city(request.getCity())
			.district(request.getDistrict())
			.street(request.getStreet())
			.zipcode(request.getZipcode())
			.streetCode(request.getStreetCode())
			.specAddress(request.getSpecAddress())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.build();

		// 좌표가 없으면 Geocoding API로 변환 (선택적)
		if (address.getLatitude() == null || address.getLongitude() == null) {
			// GeocodingResult result = geocodingService.geocode(address.getFullAddress());
			// address.updateCoordinates(result.getLatitude(), result.getLongitude());
			log.warn("좌표 정보가 없습니다. Geocoding API 연동 필요");
		}

		Address savedAddress = addressRepository.save(address);
		user.setAddress(savedAddress);

		return AddressResponse.from(savedAddress);
	}

	/**
	 * 사용자 주소 수정
	 */
	@Transactional
	public AddressResponse updateAddress(Long userId, AddressRequest request) {
		log.info("사용자 주소 수정 - userId: {}", userId);

		Address address = addressRepository.findByUser_UserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("등록된 주소가 없습니다."));

		// 주소 정보 업데이트
		address.updateAddress(
			request.getCity(),
			request.getDistrict(),
			request.getStreet(),
			request.getZipcode(),
			request.getStreetCode(),
			request.getSpecAddress()
		);

		// 좌표 업데이트 (제공된 경우)
		if (request.getLatitude() != null && request.getLongitude() != null) {
			address.updateCoordinates(request.getLatitude(), request.getLongitude());
		} else {
			// Geocoding API로 좌표 변환 (선택적)
			// GeocodingResult result = geocodingService.geocode(address.getFullAddress());
			// address.updateCoordinates(result.getLatitude(), result.getLongitude());
		}

		return AddressResponse.from(address);
	}

	/**
	 * 사용자 주소 삭제
	 */
	@Transactional
	public void deleteAddress(Long userId) {
		log.info("사용자 주소 삭제 - userId: {}", userId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		user.removeAddress();
		addressRepository.deleteByUser_UserId(userId);
	}

	/**
	 * 주소 존재 여부 확인
	 */
	public boolean hasAddress(Long userId) {
		return addressRepository.existsByUser_UserId(userId);
	}
}
