package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static nextstep.subway.acceptance.PathSteps.baseDocumentRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;

public class PathDocumentation extends Documentation {

    private static final String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc4MDk1OTk3LCJleHAiOjIwMzgwOTU5OTcsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiJdfQ.Zu9ANZjURZXSfto__4S4vOqwLDslgpUEGt-UwMMk0ks";
    @MockBean
    private PathService pathService;

    @LocalServerPort
    private Integer port;

    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10,
            20, 1450);

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        baseDocumentRequest(spec, "/distance", port)
            .header("Authorization", authorization)
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
            15, 1350);

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        baseDocumentRequest(spec, "/duration", port)
            .header("Authorization", authorization)
            .queryParam("source", 1L)
            .queryParam("target", 3L)
            .queryParam("type", PathType.DURATION)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
