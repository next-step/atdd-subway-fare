package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.PathType;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        final LocalDateTime now = LocalDateTime.now();
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", now, now),
                        new StationResponse(2L, "역삼역", now, now)
                ), 10, 4
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        두_역의_최단_거리_경로_조회를_요청(given(spec, "path"), 1L, 2L);
    }
}
