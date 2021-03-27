package nextstep.subway.path.documentation;

import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.path.documentation.PathSteps.getSpec;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    private PathResponse 강남_역삼_경로;

    @BeforeEach
    void setUp() {
        강남_역삼_경로 = new PathResponse(
                Lists.newArrayList(new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10
        );
    }

    @Test
    void path() {
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(강남_역삼_경로);

        getSpec(spec, "path")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all();
    }
}
