package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.constant.SearchType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
                10,
                5,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(SearchType.class))).thenReturn(pathResponse);

        searchType에_따른_두_역의_최단_경로_조회를_요청("pathWithDistance", SearchType.DISTANCE);
    }

    @Test
    void pathByDuration() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
                10,
                5,
                1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(SearchType.class))).thenReturn(pathResponse);

        searchType에_따른_두_역의_최단_경로_조회를_요청("pathWithDuration", SearchType.DURATION);
    }

    private ExtractableResponse<Response> searchType에_따른_두_역의_최단_경로_조회를_요청(String identifier, SearchType searchType) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("지하철역"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("searchType", searchType.name())
                .when().get("/paths")
                .then().log().all().extract();
    }
}
