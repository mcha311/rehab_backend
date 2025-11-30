package com.rehab.domain.entity;

import java.math.BigDecimal;
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
    name = "recovery_score",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "date"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecoveryScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recovery_score_id")
    private Long recoveryScoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "daily_score", precision = 5, scale = 2)
    private BigDecimal dailyScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "score_trend_7d", columnDefinition = "JSON")
    private String scoreTrend7d;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "score_trend_14d", columnDefinition = "JSON")
    private String scoreTrend14d;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "score_factors", columnDefinition = "JSON")
    private String scoreFactors;
}
