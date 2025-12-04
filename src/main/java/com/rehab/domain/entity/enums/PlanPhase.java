package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 운동 단계 Enum
 * 재활 프로그램의 진행 단계를 나타냅니다.
 */
@Getter
@RequiredArgsConstructor
public enum PlanPhase {

	/**
	 * 1단계: 초기 재활
	 * 통증 완화 및 기본 움직임 회복
	 */
	PHASE_1("1단계", "초기 재활"),

	/**
	 * 2단계: 근력 강화
	 * 근력 및 지구력 향상
	 */
	PHASE_2("2단계", "근력 강화"),

	/**
	 * 3단계: 기능 회복
	 * 일상 활동 복귀 준비
	 */
	PHASE_3("3단계", "기능 회복"),

	/**
	 * 4단계: 유지 및 예방
	 * 재발 방지 및 유지 관리
	 */
	PHASE_4("4단계", "유지 및 예방");

	private final String code;
	private final String description;
}
