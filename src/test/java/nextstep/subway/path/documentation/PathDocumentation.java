package nextstep.subway.path.documentation;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Documentation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        List<StationResponse> stations = Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now())
        );

        PathResponse pathResponse = new PathResponse(stations, 10, 10);

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        두_역의_최단_거리_경로_조회를_요청(1L, 2L);
    }
}

