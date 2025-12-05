package com.rehab.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.Gender;
import com.rehab.domain.entity.enums.UserRole;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

	@Column(name = "password", unique = true)
	private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

	@Column(name = "height")
	private Float height;

	@Column(name = "weight")
	private Float weight;

	@Column(name = "age")
	private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "current_streak")
    private Integer currentStreak;

    @Column(name = "max_streak")
    private Integer maxStreak;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @Column(name = "fcm_token")
    private String fcmToken;

	//소셜 로그인용 필드
	@Column(name = "provider")
	private String provider;  // "kakao"

	@Column(name = "provider_id", unique = true)
	private String providerId;   // 카카오의 회원 고유번호


	// 연관관계
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<SymptomIntake> symptomIntakes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RehabPlan> rehabPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseLog> exerciseLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DailySummary> dailySummaries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecoveryScore> recoveryScores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Medication> medications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicationLog> medicationLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportSnapshot> reportSnapshots = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AiInferenceLog> aiInferenceLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AuditLog> auditLogs = new ArrayList<>();

	public static User createKakaoUser(String providerId, String email, String nickname, String profileImage) {
		return User.builder()
			.provider("kakao")
			.providerId(providerId)
			.email(email)
			.role(UserRole.USER)
			.currentStreak(0)
			.maxStreak(0)
			.build();
	}

	/**
	 * 문진 정보 추가
	 */
	public void addSymptomIntake(SymptomIntake symptomIntake) {
		this.symptomIntakes.add(symptomIntake);
		// SymptomIntake 엔티티에 setUser 메서드가 있다면 설정
		// symptomIntake.setUser(this);
	}

	/**
	 * 가장 최근 문진 정보 조회
	 */
	public SymptomIntake getLatestSymptomIntake() {
		if (symptomIntakes.isEmpty()) {
			return null;
		}
		return symptomIntakes.stream()
			.max(Comparator.comparing(SymptomIntake::getCreatedAt))
			.orElse(null);
	}

	/**
	 * 문진 완료 여부 확인
	 */
	public boolean hasCompletedIntake() {
		SymptomIntake latest = getLatestSymptomIntake();
		return latest != null && latest.isIntakeCompleted();
	}

	/**
	 * 문진 이력이 있는지 확인
	 */
	public boolean hasIntakeHistory() {
		return !symptomIntakes.isEmpty();
	}
}
