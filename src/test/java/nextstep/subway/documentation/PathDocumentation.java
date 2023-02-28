package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import java.util.List;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            List.of(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ),
            10,
            10,
            1_250
        );

        when(pathService.findPath(any())).thenReturn(pathResponse);

        RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParam("type", "DISTANCE")
            .when().get("/paths")
            .then().log().all().extract();
    }
}
