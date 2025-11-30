package com.rehab.domain.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_inference_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiInferenceLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inference_log_id")
    private Long inferenceLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "model_key", nullable = false)
    private String modelKey;

    @Column(name = "model_version")
    private String modelVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_snapshot", columnDefinition = "JSON")
    private String inputSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "output_snapshot", columnDefinition = "JSON")
    private String outputSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "knowledge_references", columnDefinition = "JSON")
    private String knowledgeReferences;

    @Column(name = "latency_ms")
    private Integer latencyMs;
}
