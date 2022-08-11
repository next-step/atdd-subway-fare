package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @DisplayName("최단 거리 경로 조회 API")
    @Test
    void findPathByDistance() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                6
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        경로_조회(1L, 2L, "DISTANCE");
    }

    @DisplayName("최소 시간 경로 조회 API")
    @Test
    void findPathByDuration() {
        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ),
                10,
                4
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class))).thenReturn(pathResponse);

        경로_조회(1L, 2L, "DURATION");
    }

    private void 경로_조회(Long source, Long target, String type) {
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
