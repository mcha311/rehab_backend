package com.rehab.domain.entity;


import com.rehab.domain.entity.base.BaseEntity;
import com.rehab.domain.entity.enums.TimeOfDay;

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
@Table(name = "medi_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MediSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medi_schedule_id")
    private Long mediScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_of_day", nullable = false)
    private TimeOfDay timeOfDay;

    @Column(name = "notify")
    @Builder.Default
    private Boolean notify = true;

    @Column(name = "rrule")
    private String rrule;
}
