package nextstep.api.documentation.subway;

import static org.mockito.Mockito.when;

import static nextstep.api.acceptance.subway.path.PathSteps.최단경로조회_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.applicaion.path.dto.PathResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.path.PathSelection;

class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        final var response = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "삼성역")
                ), 10
        );

        when(pathService.findShortestPath(1L, 2L, PathSelection.DISTANCE.name())).thenReturn(response);

        최단경로조회_요청(1L, 2L, PathSelection.DISTANCE.name(), makeRequestSpec("path"));
    }
}
