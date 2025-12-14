package com.rehab.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 운동 시설 유형
 */
@Getter
@RequiredArgsConstructor
public enum FacilityType {

	// 공공 체육 시설
	PUBLIC_GYM("공공 체육관", "지자체 운영 체육관"),
	PUBLIC_POOL("공공 수영장", "지자체 운영 수영장"),
	PUBLIC_PARK("공원 운동시설", "공원 내 운동 시설"),
	COMMUNITY_CENTER("주민센터 체육시설", "주민센터/복지관 운동시설"),

	// 상업 시설
	FITNESS_CENTER("피트니스 센터", "일반 헬스장/PT센터"),
	PILATES_STUDIO("필라테스 스튜디오", "필라테스 전문 스튜디오"),
	YOGA_STUDIO("요가 스튜디오", "요가 전문 스튜디오"),
	SWIMMING_POOL("수영장", "상업 수영장"),
	SPORTS_CENTER("종합 스포츠센터", "다목적 스포츠 시설"),

	// 전문 재활 시설
	REHAB_CENTER("재활 센터", "전문 재활 치료 센터"),
	PHYSICAL_THERAPY("물리치료실", "물리치료 전문"),
	MEDICAL_FITNESS("메디컬 피트니스", "의료 연계 운동 시설"),

	// 특화 시설
	CLIMBING_GYM("클라이밍 센터", "암벽등반 시설"),
	MARTIAL_ARTS("무술 도장", "태권도/유도/검도 등"),
	DANCE_STUDIO("댄스 스튜디오", "댄스 전문"),
	GOLF_RANGE("골프 연습장", "골프 연습장"),

	// 야외 시설
	OUTDOOR_GYM("야외 운동시설", "야외 운동기구"),
	RUNNING_TRACK("육상 트랙", "육상 트랙/런닝 코스"),
	SPORTS_PARK("체육공원", "종합 체육공원"),

	// 기타
	OTHER("기타", "기타 운동 시설");

	private final String displayName;
	private final String description;

	/**
	 * 재활에 적합한 시설 유형 판별
	 */
	public boolean isSuitableForRehab() {
		return this == REHAB_CENTER ||
			this == PHYSICAL_THERAPY ||
			this == MEDICAL_FITNESS ||
			this == PUBLIC_GYM ||
			this == PUBLIC_POOL ||
			this == PILATES_STUDIO ||
			this == YOGA_STUDIO;
	}

	/**
	 * 공공 시설 여부
	 */
	public boolean isPublicFacility() {
		return this == PUBLIC_GYM ||
			this == PUBLIC_POOL ||
			this == PUBLIC_PARK ||
			this == COMMUNITY_CENTER ||
			this == OUTDOOR_GYM ||
			this == RUNNING_TRACK ||
			this == SPORTS_PARK;
	}

	/**
	 * 무료/저렴한 시설 여부
	 */
	public boolean isLowCost() {
		return isPublicFacility();
	}
}
