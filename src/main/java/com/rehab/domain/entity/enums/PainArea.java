package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 통증 부위 열거형
 * 사용자의 통증이 발생하는 신체 부위를 나타냄
 */
@Getter
@RequiredArgsConstructor
public enum PainArea {

	// 목/어깨
	NECK("목", "Neck"),
	SHOULDER("어깨", "Shoulder"),
	UPPER_BACK("상부 등", "Upper Back"),

	// 허리/골반
	LOWER_BACK("허리", "Lower Back"),
	PELVIS("골반", "Pelvis"),
	HIP("고관절", "Hip"),

	// 상지
	ELBOW("팔꿈치", "Elbow"),
	WRIST("손목", "Wrist"),
	HAND("손", "Hand"),

	// 하지
	KNEE("무릎", "Knee"),
	ANKLE("발목", "Ankle"),
	FOOT("발", "Foot"),

	// 기타
	CHEST("가슴", "Chest"),
	ABDOMEN("복부", "Abdomen"),
	FULL_BODY("전신", "Full Body"),
	OTHER("기타", "Other");

	private final String korean;
	private final String english;

	/**
	 * 한글 이름으로 PainArea 찾기
	 */
	public static PainArea fromKorean(String korean) {
		for (PainArea area : values()) {
			if (area.korean.equals(korean)) {
				return area;
			}
		}
		throw new IllegalArgumentException("Unknown pain area: " + korean);
	}

	/**
	 * 영문 이름으로 PainArea 찾기
	 */
	public static PainArea fromEnglish(String english) {
		for (PainArea area : values()) {
			if (area.english.equalsIgnoreCase(english)) {
				return area;
			}
		}
		throw new IllegalArgumentException("Unknown pain area: " + english);
	}
}
