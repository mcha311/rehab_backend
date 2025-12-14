package com.rehab.domain.repository.address;

import com.rehab.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 주소 Repository
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	/**
	 * 사용자 ID로 주소 조회
	 */
	Optional<Address> findByUser_UserId(Long userId);

	/**
	 * 사용자 ID로 주소 존재 여부 확인
	 */
	boolean existsByUser_UserId(Long userId);

	/**
	 * 사용자 ID로 주소 삭제
	 */
	void deleteByUser_UserId(Long userId);
}
