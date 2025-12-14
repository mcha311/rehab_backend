package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 즐겨찾기 운동 시설
 * User : ExerciseFacility = N : M 관계
 */
@Entity
@Table(name = "user_facility_favorite",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_facility", columnNames = {"user_id", "facility_id"})
	},
	indexes = {
		@Index(name = "idx_user", columnList = "user_id"),
		@Index(name = "idx_facility", columnList = "facility_id")
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserFacilityFavorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_id")
	private Long favoriteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id", nullable = false)
	private ExerciseFacility facility;

	@Column(name = "memo")
	private String memo;  // 개인 메모 (예: "집 근처 헬스장", "주말에만 이용")

	@Column(name = "visit_count")
	@Builder.Default
	private Integer visitCount = 0;  // 방문 횟수

	@Column(name = "is_active")
	@Builder.Default
	private Boolean isActive = true;  // 활성 상태

	// ================================
	// 연관관계 편의 메서드
	// ================================

	public void setUser(User user) {
		this.user = user;
	}

	public void setFacility(ExerciseFacility facility) {
		this.facility = facility;
	}

	/**
	 * 정적 팩토리 메서드
	 */
	public static UserFacilityFavorite create(User user, ExerciseFacility facility) {
		UserFacilityFavorite favorite = UserFacilityFavorite.builder()
			.user(user)
			.facility(facility)
			.build();

		user.addFacilityFavorite(favorite);
		facility.getFavorites().add(favorite);

		return favorite;
	}

	/**
	 * 메모 업데이트
	 */
	public void updateMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 방문 횟수 증가
	 */
	public void incrementVisitCount() {
		this.visitCount++;
	}

	/**
	 * 즐겨찾기 활성화/비활성화
	 */
	public void activate() {
		this.isActive = true;
	}

	public void deactivate() {
		this.isActive = false;
	}
}
