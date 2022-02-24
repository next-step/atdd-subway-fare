package nextstep.subway.documentation;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", null, null),
                new StationResponse(2L, "역삼역", null, null)
            ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
