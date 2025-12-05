package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.ExerciseExperience;
import com.rehab.domain.entity.enums.PainArea;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문진(Symptom Intake) 정보 엔티티
 * 사용자의 통증 부위, 강도, 목표, 운동 경험 등을 저장
 */
@Entity
@Table(name = "symptom_intake")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymptomIntake extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intake_id")
	private Long intakeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/**
	 * 통증 부위
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "pain_area", length = 50)
	private PainArea painArea;

	/**
	 * 통증 강도 (1-10)
	 */
	@Column(name = "pain_level")
	private Integer painLevel;

	/**
	 * 재활 목표
	 */
	@Column(name = "goal", columnDefinition = "TEXT")
	private String goal;

	/**
	 * 운동 경험 수준
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "exercise_experience", length = 30)
	private ExerciseExperience exerciseExperience;

	// === 비즈니스 메서드 ===

	/**
	 * 문진 정보 업데이트
	 */
	public void updateIntake(PainArea painArea, Integer painLevel, String goal, ExerciseExperience exerciseExperience) {
		if (painArea != null) {
			this.painArea = painArea;
		}
		if (painLevel != null) {
			this.painLevel = painLevel;
		}
		if (goal != null) {
			this.goal = goal;
		}
		if (exerciseExperience != null) {
			this.exerciseExperience = exerciseExperience;
		}
	}

	/**
	 * 통증 강도가 유효한 범위인지 확인
	 */
	public boolean isValidPainLevel() {
		return painLevel != null && painLevel >= 1 && painLevel <= 10;
	}

	/**
	 * 초기 문진이 완료되었는지 확인
	 */
	public boolean isIntakeCompleted() {
		return painArea != null && painLevel != null && exerciseExperience != null;
	}
}
