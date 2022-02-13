package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathStep.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.documentation.given.PathSnippet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        LocalDateTime DUMMY_DATE = LocalDateTime.now();
        PathResponse pathResponse = new PathResponse(
            Arrays.asList(
                new StationResponse(1L, "구리역", DUMMY_DATE, DUMMY_DATE),
                new StationResponse(2L, "수원역", DUMMY_DATE, DUMMY_DATE)
            ),
            100
        );
        when(pathService.findPath(anyLong(), anyLong()))
            .thenReturn(pathResponse);

        지하철_최단_거리_조회_요청(
            PathSnippet.PATH.toGiven(spec, "path"),
            1L, 2L
        );
    }
}
