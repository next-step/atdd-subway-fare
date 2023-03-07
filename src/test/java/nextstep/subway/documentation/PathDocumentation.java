package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathType;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("최단 거리 경로 조회")
    void path_distance() {
        // given
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(3L, "선릉역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                8
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        // when
        // then
        두_역의_경로_조회_요청(1L, 2L, DISTANCE_TYPE, pathParametersDocumentSpec("path/success/distance"));
    }

    @Test
    @DisplayName("최소 시간 경로 조회")
    void path_duration() {
        // given
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(4L, "삼성역"),
                        new StationResponse(2L, "역삼역")
                ),
                15,
                5
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        // when
        // then
        두_역의_경로_조회_요청(1L, 2L, DURATION_TYPE, pathParametersDocumentSpec("path/success/duration"));
    }

    @Test
    @DisplayName("경로 조회 실패-같은 출발역과 도착역")
    void path_sameStation() {
        // given
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenThrow(IllegalArgumentException.class);

        // when
        // then
        두_역의_경로_조회_요청(1L, 1L, DISTANCE_TYPE, basicDocumentSpec("path/failure/sameStation"));
    }

    @Test
    @DisplayName("경로 조회 실패-같은 출발역과 도착역")
    void path_notLinked() {
        // given
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenThrow(IllegalArgumentException.class);

        // when
        // then
        두_역의_경로_조회_요청(1L, 3L, DISTANCE_TYPE, basicDocumentSpec("path/failure/notLinked"));
    }
}
