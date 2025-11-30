package com.rehab.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.ReportPeriod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReportSnapshot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_snapshot_id")
    private Long reportSnapshotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private ReportPeriod period;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "covered_range", columnDefinition = "JSON", nullable = false)
    private String coveredRange;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics", columnDefinition = "JSON")
    private String metrics;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "weekly_highlight", columnDefinition = "JSON")
    private String weeklyHighlight;

    @Column(name = "recovery_prediction", precision = 5, scale = 2)
    private BigDecimal recoveryPrediction;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
