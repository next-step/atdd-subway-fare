package nextstep.subway.documentation;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import nextstep.subway.acceptance.PathStep;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.documentation.snippet.PathSnippet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    private PathResponse createPathResponse(int distance, int duration) {
        LocalDateTime DUMMY_DATE = LocalDateTime.now();
        return new PathResponse(
            Arrays.asList(
                new StationResponse(1L, "구리역", DUMMY_DATE, DUMMY_DATE),
                new StationResponse(2L, "수원역", DUMMY_DATE, DUMMY_DATE)
            ),
            distance, duration
        );
    }

    @Test
    void pathByDistance() {
        when(pathService.findPath(anyLong(), anyLong(), any()))
            .thenReturn(createPathResponse(100, 200));

        PathStep.두_역의_최단_거리_경로_조회를_요청(
            PathSnippet.PATH.toGiven(spec, DocumentationName.PATH_BY_DISTANCE.name()),
            1L, 2L
        );
    }

    @Test
    void pathByDuration() {
        when(pathService.findPath(anyLong(), anyLong(), any()))
            .thenReturn(createPathResponse(200, 100));

        PathStep.두_역의_최소_시간_경로_조회를_요청(
            PathSnippet.PATH.toGiven(spec, DocumentationName.PATH_BY_DURATION.name()),
            1L, 2L
        );
    }
}
