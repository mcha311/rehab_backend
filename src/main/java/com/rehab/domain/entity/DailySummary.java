package com.rehab.domain.entity;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rehab.domain.entity.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "daily_summary",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "date"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailySummary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Long summaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "all_exercises_completed")
    @Builder.Default
    private Boolean allExercisesCompleted = false;

    @Column(name = "exercise_completion_rate")
    @Builder.Default
    private Integer exerciseCompletionRate = 0;

    @Column(name = "all_medications_taken")
    @Builder.Default
    private Boolean allMedicationsTaken = false;

    @Column(name = "medication_completion_rate")
    @Builder.Default
    private Integer medicationCompletionRate = 0;

    @Column(name = "avg_pain_score")
    private Integer avgPainScore;

    @Column(name = "total_duration_sec")
    @Builder.Default
    private Integer totalDurationSec = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "daily_metrics", columnDefinition = "JSON")
    private String dailyMetrics;
}
