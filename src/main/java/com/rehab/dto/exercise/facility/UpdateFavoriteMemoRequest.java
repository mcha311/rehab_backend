package com.rehab.dto.exercise.facility;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 즐겨찾기 메모 수정 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "즐겨찾기 메모 수정 요청")
public class UpdateFavoriteMemoRequest {

	@Schema(description = "메모", required = true, example = "주말에만 이용하는 필라테스")
	private String memo;
}
