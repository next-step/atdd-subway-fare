package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.Lists;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;

public class PathStubs {
	public static void 최단_거리_경로_조회_시_성공_응답을_반환(PathService pathService) {
		PathResponse pathResponse = new PathResponse(
			Lists.newArrayList(
				new StationResponse(1L, "강남역"),
				new StationResponse(2L, "역삼역")
			), 10, 12);

		when(pathService.findPath(anyLong(), anyLong(), eq(PathType.DISTANCE))).thenReturn(pathResponse);
	}

	public static void 최소_시간_경로_조회_시_성공_응답을_반환(PathService pathService) {
		PathResponse pathResponse = new PathResponse(
			Lists.newArrayList(
				new StationResponse(1L, "강남역"),
				new StationResponse(2L, "역삼역")
			), 10, 12);

		when(pathService.findPath(anyLong(), anyLong(), eq(PathType.DURATION))).thenReturn(pathResponse);
	}
}
