package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.springframework.http.HttpStatus;

public class PathSteps {
    private PathSteps() {
    }

    public static ExtractableResponse<Response> 경로_조회를_요청한다(final Long source, final Long target) {
        return RestAssured.given().log().all()
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", PathType.DISTANCE)
                .when().get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
