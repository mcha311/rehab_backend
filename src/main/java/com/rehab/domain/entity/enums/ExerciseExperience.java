package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 운동 경험 수준 열거형
 * 사용자의 운동 경험 정도를 나타냄
 */
@Getter
@RequiredArgsConstructor
public enum ExerciseExperience {

	/**
	 * 초보자 - 운동 경험이 거의 없거나 오랜만에 운동을 시작하는 경우
	 */
	BEGINNER("초보자", "Beginner", "운동 경험이 거의 없거나 오랜만에 시작", 1),

	/**
	 * 초급 - 가벼운 운동을 규칙적으로 하고 있는 경우
	 */
	NOVICE("초급", "Novice", "가벼운 운동을 규칙적으로 수행", 2),

	/**
	 * 중급 - 적당한 강도의 운동을 꾸준히 하고 있는 경우
	 */
	INTERMEDIATE("중급", "Intermediate", "중간 강도 운동을 꾸준히 수행", 3),

	/**
	 * 고급 - 강도 높은 운동을 규칙적으로 하고 있는 경우
	 */
	ADVANCED("고급", "Advanced", "고강도 운동을 규칙적으로 수행", 4),

	/**
	 * 전문가 - 운동 선수 수준이거나 전문적인 트레이닝 경험이 있는 경우
	 */
	EXPERT("전문가", "Expert", "운동 선수 수준 또는 전문 트레이닝 경험", 5);

	private final String korean;
	private final String english;
	private final String description;
	private final int level;

	/**
	 * 한글 이름으로 ExerciseExperience 찾기
	 */
	public static ExerciseExperience fromKorean(String korean) {
		for (ExerciseExperience exp : values()) {
			if (exp.korean.equals(korean)) {
				return exp;
			}
		}
		throw new IllegalArgumentException("Unknown exercise experience: " + korean);
	}

	/**
	 * 영문 이름으로 ExerciseExperience 찾기
	 */
	public static ExerciseExperience fromEnglish(String english) {
		for (ExerciseExperience exp : values()) {
			if (exp.english.equalsIgnoreCase(english)) {
				return exp;
			}
		}
		throw new IllegalArgumentException("Unknown exercise experience: " + english);
	}

	/**
	 * 레벨로 ExerciseExperience 찾기
	 */
	public static ExerciseExperience fromLevel(int level) {
		for (ExerciseExperience exp : values()) {
			if (exp.level == level) {
				return exp;
			}
		}
		throw new IllegalArgumentException("Unknown exercise experience level: " + level);
	}

	/**
	 * 다음 레벨로 진행 가능한지 확인
	 */
	public boolean canProgress() {
		return this != EXPERT;
	}

	/**
	 * 다음 레벨 반환
	 */
	public ExerciseExperience nextLevel() {
		if (!canProgress()) {
			return this;
		}
		return fromLevel(this.level + 1);
	}

	/**
	 * 이전 레벨로 낮출 수 있는지 확인
	 */
	public boolean canRegress() {
		return this != BEGINNER;
	}

	/**
	 * 이전 레벨 반환
	 */
	public ExerciseExperience previousLevel() {
		if (!canRegress()) {
			return this;
		}
		return fromLevel(this.level - 1);
	}
}






