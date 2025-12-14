package com.rehab.dto.exercise.facility;

import java.util.List;

import com.rehab.domain.entity.FacilityImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시설 이미지 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "시설 이미지")
public class FacilityImageDto {

	@Schema(description = "이미지 ID")
	private Long facilityImageId;

	@Schema(description = "이미지 제목")
	private String title;

	@Schema(description = "이미지 URL 목록")
	private List<String> imageUrl;

	@Schema(description = "대표 이미지 여부")
	private Boolean isMain;

	public FacilityImageDto(FacilityImage image) {
		this.facilityImageId = image.getFacilityImageId();
		this.title = image.getTitle();
		this.imageUrl = image.getImageUrl();
		this.isMain = image.getIsMain();
	}
}
