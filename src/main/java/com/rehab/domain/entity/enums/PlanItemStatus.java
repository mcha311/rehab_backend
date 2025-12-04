package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 플랜 항목 상태 Enum
 *
 * 개별 운동 항목의 활성 상태를 나타냅니다.
 */
@Getter
@RequiredArgsConstructor
public enum PlanItemStatus {

	/**
	 * 활성: 현재 진행 중인 항목
	 */
	ACTIVE("활성", "현재 진행 중"),

	/**
	 * 일시정지: 일시적으로 중단된 항목
	 */
	PAUSED("일시정지", "일시적으로 중단"),

	/**
	 * 완료: 목표를 달성하여 완료된 항목
	 */
	COMPLETED("완료", "목표 달성"),

	/**
	 * 취소: 더 이상 진행하지 않는 항목
	 */
	CANCELLED("취소", "진행 취소");

	private final String code;
	private final String description;
}
