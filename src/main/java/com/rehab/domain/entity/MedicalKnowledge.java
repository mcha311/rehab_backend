package com.rehab.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.EvidenceLevel;
import com.rehab.domain.entity.enums.MedicalKnowledgeCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical_knowledge")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MedicalKnowledge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "knowledge_id")
    private Long knowledgeId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MedicalKnowledgeCategory category;

    @Column(name = "source")
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(name = "evidence_level")
    private EvidenceLevel evidenceLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    // 연관관계
    @OneToMany(mappedBy = "medicalKnowledge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseEvidence> exerciseEvidences = new ArrayList<>();
}
