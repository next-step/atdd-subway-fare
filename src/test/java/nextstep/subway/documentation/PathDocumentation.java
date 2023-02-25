package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void pathDefault() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(4L, "양재역")
                ), 10, 10
        );

        BDDMockito.given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        given("pathDefault")
                .queryParam("source", 1L)
                .queryParam("target", 4L)
                .when().get("/paths")
                .then().log().all().extract();
    }

    @Test
    void pathDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "교대역"),
                        new StationResponse(3L, "남부터미널"),
                        new StationResponse(4L, "양재역")
                ), 10, 5
        );

        BDDMockito.given(pathService.findPath(anyLong(), anyLong(), any())).willReturn(pathResponse);

        given("pathDuration")
                .queryParam("source", 1L)
                .queryParam("target", 4L)
                .queryParams("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
