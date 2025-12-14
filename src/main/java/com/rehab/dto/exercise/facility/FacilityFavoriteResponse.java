package com.rehab.dto.exercise.facility;

import java.time.LocalDateTime;

import com.rehab.domain.entity.ExerciseFacility;
import com.rehab.domain.entity.FacilityImage;
import com.rehab.domain.entity.UserFacilityFavorite;
import com.rehab.domain.entity.enums.FacilityType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 즐겨찾기 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "즐겨찾기 시설")
public class FacilityFavoriteResponse {

	@Schema(description = "즐겨찾기 ID")
	private Long favoriteId;

	@Schema(description = "시설 ID")
	private Long facilityId;

	@Schema(description = "시설명")
	private String name;

	@Schema(description = "시설 유형")
	private FacilityType facilityType;

	@Schema(description = "시설 유형 표시명")
	private String facilityTypeDisplayName;

	@Schema(description = "주소")
	private String address;

	@Schema(description = "전화번호")
	private String phoneNumber;

	@Schema(description = "메모")
	private String memo;

	@Schema(description = "방문 횟수")
	private Integer visitCount;

	@Schema(description = "즐겨찾기 등록일")
	private LocalDateTime favoritedAt;

	@Schema(description = "대표 이미지 URL")
	private String mainImageUrl;

	public FacilityFavoriteResponse(UserFacilityFavorite favorite) {
		ExerciseFacility facility = favorite.getFacility();

		this.favoriteId = favorite.getFavoriteId();
		this.facilityId = facility.getFacilityId();
		this.name = facility.getName();
		this.facilityType = facility.getFacilityType();
		this.facilityTypeDisplayName = facility.getFacilityType().getDisplayName();
		this.address = facility.getFullAddress();
		this.phoneNumber = facility.getPhoneNumber();
		this.memo = favorite.getMemo();
		this.visitCount = favorite.getVisitCount();
		this.favoritedAt = favorite.getCreatedAt();

		this.mainImageUrl = facility.getImages().stream()
			.filter(FacilityImage::getIsMain)
			.findFirst()
			.map(img -> img.getImageUrl() != null && !img.getImageUrl().isEmpty()
				? img.getImageUrl().get(0)
				: null)
			.orElse(null);
	}
}
