package com.rehab.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.rehab.domain.entity.base.BaseEntity;

/**
 * 사용자 연속 달성(Streak) 엔티티
 * - 사용자당 1개 레코드 (스냅샷 데이터)
 * - 매일 DailySummary 확정 시 업데이트
 * - 홈 화면 조회 시 즉시 반환용
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_streak", indexes = {
	@Index(name = "idx_last_active_date", columnList = "lastActiveDate")
})
public class UserStreak extends BaseEntity {

	@Id
	private Long userId;  // User PK와 동일하게 사용

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId  // userId를 PK이자 FK로 사용
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 현재 연속 달성 일수
	 */
	@Column(nullable = false)
	private Integer currentStreak;

	/**
	 * 최대 연속 달성 일수 (역대 최고 기록)
	 */
	@Column(nullable = false)
	private Integer maxStreak;

	/**
	 * 마지막 활동 날짜
	 * - 활동 기준: 운동 완료율 ≥ 60% or 복약 완료율 ≥ 70%
	 */
	@Column(nullable = false)
	private LocalDate lastActiveDate;

	@Builder
	public UserStreak(User user, Integer currentStreak, Integer maxStreak, LocalDate lastActiveDate) {
		this.user = user;
		this.currentStreak = currentStreak != null ? currentStreak : 0;
		this.maxStreak = maxStreak != null ? maxStreak : 0;
		this.lastActiveDate = lastActiveDate;
	}

	/**
	 * 신규 사용자용 초기 Streak 생성
	 */
	public static UserStreak createInitial(User user) {
		return UserStreak.builder()
			.user(user)
			.currentStreak(0)
			.maxStreak(0)
			.lastActiveDate(LocalDate.now())
			.build();
	}

	/**
	 * Streak 업데이트 (활동 달성 시)
	 * @param today 오늘 날짜
	 * @return 업데이트된 currentStreak
	 */
	public int incrementStreak(LocalDate today) {
		if (this.lastActiveDate.equals(today.minusDays(1))) {
			// 어제에 이어서 오늘도 달성 → streak++
			this.currentStreak++;
		} else if (this.lastActiveDate.isBefore(today.minusDays(1))) {
			// 하루 이상 끊김 → streak 리셋
			this.currentStreak = 1;
		}
		// lastActiveDate == today인 경우는 같은 날 중복 호출 (변경 없음)

		this.lastActiveDate = today;
		this.maxStreak = Math.max(this.maxStreak, this.currentStreak);

		return this.currentStreak;
	}

	/**
	 * Streak 초기화 (활동 기준 미달 시)
	 * @param today 오늘 날짜
	 */
	public void resetStreak(LocalDate today) {
		if (!this.lastActiveDate.equals(today)) {
			this.currentStreak = 0;
			this.lastActiveDate = today;
		}
	}

	/**
	 * 오늘이 연속 달성 중인지 확인
	 */
	public boolean isActiveToday(LocalDate today) {
		return this.lastActiveDate.equals(today) && this.currentStreak > 0;
	}

	/**
	 * 어제까지 연속 달성 중이었는지 확인 (오늘 아직 미체크)
	 */
	public boolean wasActiveYesterday(LocalDate today) {
		return this.lastActiveDate.equals(today.minusDays(1)) && this.currentStreak > 0;
	}
}
