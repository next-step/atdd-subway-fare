package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "구의역"),
                        new StationResponse(2L, "선릉역")),
                10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path", requestParameters(
                        parameterWithName("target").description("조회할 경로의 출발역"),
                        parameterWithName("source").description("조회할 경로의 목적지"),
                        parameterWithName("type").description("최단경로 구분 기준")
                        )))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
