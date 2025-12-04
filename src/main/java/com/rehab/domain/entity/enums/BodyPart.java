package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 운동 대상 신체 부위
 */
@Getter
@RequiredArgsConstructor
public enum BodyPart {
	LOWER_BACK("허리"),
	NECK("목"),
	SHOULDER("어깨"),
	KNEE("무릎"),
	ANKLE("발목"),
	HIP("고관절"),
	ELBOW("팔꿈치"),
	WRIST("손목"),
	FULL_BODY("전신");

	private final String description;
}
