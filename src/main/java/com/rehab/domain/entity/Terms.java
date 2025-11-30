package com.rehab.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.rehab.domain.entity.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "terms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Terms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long termsId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @OneToMany(mappedBy = "terms", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Consent> consents = new ArrayList<>();
}
