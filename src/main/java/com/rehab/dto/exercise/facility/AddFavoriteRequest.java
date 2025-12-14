package com.rehab.dto.exercise.facility;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 즐겨찾기 추가 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "즐겨찾기 추가 요청")
public class AddFavoriteRequest {

	@Schema(description = "시설 ID", required = true)
	private Long facilityId;

	@Schema(description = "메모", example = "집 근처 헬스장")
	private String memo;
}
