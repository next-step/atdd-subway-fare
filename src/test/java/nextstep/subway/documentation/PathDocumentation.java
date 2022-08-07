package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("최단 거리 조회")
    void pathDistance() {
        //given
        PathResponse pathResponse = new PathResponse(
            List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
            10,
            5
        );

        //when
        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        //then
        최단_거리를_조회한다();
    }

    private ExtractableResponse<Response> 최단_거리를_조회한다() {
        return RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    @Test
    @DisplayName("최소 시간 조회")
    void pathDuration() {
        //given
        PathResponse pathResponse = new PathResponse(
            List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "역삼역")),
            10,
            5
        );

        //when
        when(pathService.findMinimumTimePath(anyLong(), anyLong())).thenReturn(pathResponse);

        //then
        최소_시간으로_조회한다();
    }

    private void 최소_시간으로_조회한다() {
        RestAssured
            .given(spec).log().all()
            .filter(document("path-time",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .when().get("/paths/time")
            .then().log().all()
            .extract();
    }

}
