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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void pathByDistance() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", null, null),
                        new StationResponse(2L, "역삼역", null, null)
                ), 10, 9
        );

        when(pathService.findPath(anyLong(), anyLong(), eq("distance"))).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path/distance",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "distance")
                .when().get("/paths")
                .then().log().all().extract();
    }

    @Test
    void pathByDuration() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", null, null),
                        new StationResponse(2L, "역삼역", null, null)
                ), 10, 9
        );

        when(pathService.findPath(anyLong(), anyLong(), eq("duration"))).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path/duration",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "duration")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
