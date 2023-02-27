package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.baseDocumentRequest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;


    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10,
            20);

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        baseDocumentRequest(spec, "/distance")
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParam("type", PathType.DISTANCE)
            .when().get("/paths")
            .then().log().all().extract();
    }

    @Test
    void pathByDuration() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(3L, "양재역")
            ), 15,
            15);

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        baseDocumentRequest(spec, "/duration")
            .queryParam("source", 1L)
            .queryParam("target", 3L)
            .queryParam("type", PathType.DURATION)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
