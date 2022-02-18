package nextstep.subway.documentation;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import nextstep.subway.acceptance.PathStep;
import nextstep.subway.applicaion.PathFacade;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.documentation.snippet.PathSnippet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("Path 문서화")
public class PathDocumentation extends Documentation {
    @MockBean
    private PathFacade pathFacade;

    private PathResponse createPathResponse(int distance, int duration, int totalCost, LocalDateTime arrivalTime) {
        LocalDateTime DUMMY_DATE = LocalDateTime.now();
        return new PathResponse(
            Arrays.asList(
                new StationResponse(1L, "구리역", DUMMY_DATE, DUMMY_DATE),
                new StationResponse(2L, "수원역", DUMMY_DATE, DUMMY_DATE)
            ),
            distance, duration, totalCost, arrivalTime
        );
    }

    private PathResponse createPathResponse(int distance, int duration, int totalCost) {
        return createPathResponse(distance, duration, totalCost, null);
    }

    @Test
    void pathByDistance() {
        when(pathFacade.findPathByDistance(anyLong(), anyLong(), any()))
            .thenReturn(createPathResponse(100, 200, 10000));

        PathStep.두_역의_최단_거리_경로_조회를_요청(
            PathSnippet.PATH.toGiven(spec, DocumentationName.PATH_BY_DISTANCE.name()),
            1L, 2L
        );
    }

    @Test
    void pathByDuration() {
        when(pathFacade.findPathByDuration(anyLong(), anyLong(), any()))
            .thenReturn(createPathResponse(200, 100, 20000));

        PathStep.두_역의_최소_시간_경로_조회를_요청(
            PathSnippet.PATH.toGiven(spec, DocumentationName.PATH_BY_DURATION.name()),
            1L, 2L
        );
    }

    @Test
    void pathByArrivalTime() {
        LocalDateTime arrivalTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 10));
        when(pathFacade.findPathByArrivalTime(anyLong(), anyLong(), any(), any()))
            .thenReturn(createPathResponse(200, 100, 20000, arrivalTime));

        PathStep.가장_빠른_도착_경로_조회를_요청(
            PathSnippet.PATH_BY_ARRIVAL_TIME.toGiven(spec, DocumentationName.PATH_BY_ARRIVAL_TIME.name()),
            1L, 2L, LocalTime.of(10, 0)
        );
    }
}
