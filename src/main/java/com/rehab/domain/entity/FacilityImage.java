package com.rehab.domain.entity;

import com.rehab.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

/**
 * 운동 시설 이미지 엔티티
 */
@Entity
@Table(name = "facility_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FacilityImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_image_id")
	private Long facilityImageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id", nullable = false)
	private ExerciseFacility facility;

	@Column(name = "title")
	private String title;  // 이미지 제목

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "image_url", columnDefinition = "json")
	private List<String> imageUrl;  // 이미지 URL 리스트

	@Column(name = "is_main")
	@Builder.Default
	private Boolean isMain = false;  // 대표 이미지 여부

	@Column(name = "display_order")
	private Integer displayOrder;  // 표시 순서

	// ================================
	// 연관관계 편의 메서드
	// ================================

	public void setFacility(ExerciseFacility facility) {
		this.facility = facility;
	}

	/**
	 * 대표 이미지로 설정
	 */
	public void setAsMain() {
		this.isMain = true;
	}

	/**
	 * 일반 이미지로 변경
	 */
	public void unsetMain() {
		this.isMain = false;
	}
}







