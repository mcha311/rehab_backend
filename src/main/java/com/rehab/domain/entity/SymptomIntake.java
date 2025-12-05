package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.ExerciseExperience;
import com.rehab.domain.entity.enums.PainArea;
import com.rehab.dto.intake.IntakeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Table(name = "symptom_intake")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SymptomIntake extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intake_id")
	private Long intakeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "pain_area", length = 50)
	private PainArea painArea;

	@Column(name = "pain_level")
	private Integer painLevel;

	@Column(name = "goal")
	private String goal;

	@Enumerated(EnumType.STRING)
	@Column(name = "exercise_experience", length = 50)
	private ExerciseExperience exerciseExperience;

	public void updateFromRequest(IntakeDto.IntakeRequest request) {
		this.painArea = request.getPainArea();
		this.painLevel = request.getPainLevel();
		this.goal = request.getGoal();
		this.exerciseExperience = request.getExerciseExperience();
	}

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
	 * 초기 문진이 완료되었는지 확인
	 */
	public boolean isIntakeCompleted() {
		return painArea != null && painLevel != null && exerciseExperience != null;
	}
}
