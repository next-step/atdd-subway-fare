package nextstep.documentation.subway;

import static org.mockito.Mockito.when;

import static nextstep.api.subway.acceptance.path.PathSteps.최단경로조회_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;

import nextstep.api.subway.applicaion.path.PathService;
import nextstep.api.subway.applicaion.path.dto.PathResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.documentation.Documentation;

@ExtendWith(RestDocumentationExtension.class)
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

        when(pathService.findShortestPath(1L, 2L)).thenReturn(response);

        최단경로조회_요청(1L, 2L, makeRequestSpec("path"));
    }
}
