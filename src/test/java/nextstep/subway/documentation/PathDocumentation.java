package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {

        PathResponse pathResponse = new PathResponse(
                List.of(
                        new StationResponse(1L, "교대역"),
                        new StationResponse(2L, "강남역"),
                        new StationResponse(3L, "양재역")
                ),
                22,
                3,
                1550
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(restDocumentationFilter("path",
                        requestParameters(
                                parameterWithName("source").description("출발역 id"),
                                parameterWithName("target").description("도착역 id"),
                                parameterWithName("type").description("경로조회 기준(DISTANCE, DURATION)")
                        ),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로역"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역명"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 소요시간"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
                        )))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 3L)
                .queryParam("type", "duration")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
