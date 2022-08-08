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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
                10,
                5
        );

        when(pathService.findPath(anyLong(), anyLong(), any(SearchType.class))).thenReturn(pathResponse);

        searchType에_따른_두_역의_최단_경로_조회를_요청("distance");
    }

    @Test
    void pathByDuration() {
        PathResponse pathResponse = new PathResponse(
                List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
                10,
                5
        );

        when(pathService.findPath(anyLong(), anyLong(), any(SearchType.class))).thenReturn(pathResponse);

        searchType에_따른_두_역의_최단_경로_조회를_요청("duration");
    }

    private ExtractableResponse<Response> searchType에_따른_두_역의_최단_경로_조회를_요청(String searchType) {
        return RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("searchType", searchType)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
