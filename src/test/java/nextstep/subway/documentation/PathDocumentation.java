package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로를_조회요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("최단거리 기준 경로 조회 API")
    void pathDistance() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 8
        );

        when(pathService.findPath(any())).thenReturn(pathResponse);

        두_역의_경로를_조회요청(givenRequestSpecification("pathDuration"), 1L, 2L, "DISTANCE");
    }

    @Test
    @DisplayName("최소시간 기준 경로 조회 API")
    void pathDuration() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 8
        );

        when(pathService.findPath(any())).thenReturn(pathResponse);

        두_역의_경로를_조회요청(givenRequestSpecification("pathDuration"), 1L, 2L, "DURATION");
    }
}
