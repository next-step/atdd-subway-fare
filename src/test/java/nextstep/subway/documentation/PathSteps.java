package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

public class PathSteps {
    public static void 경로_조회_요청(RequestSpecification spec) {
        RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", PathType.DISTANCE)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
