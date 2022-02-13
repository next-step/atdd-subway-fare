package nextstep.subway.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 지하철_최단_거리_조회_요청(RequestSpecification given, long source, long target) {
        return given.log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .when().get("/paths")
                    .then().log().all().extract();
    }
}
