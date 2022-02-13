package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification given, long source, long target) {
        return given.log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .when().get("/paths/distance")
                    .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(long source, long target) {
        return 두_역의_최단_거리_경로_조회를_요청(RestAssured.given(), source, target);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(RequestSpecification given, long source, long target) {
        return given.log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .when().get("/paths/duration")
                    .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(long source, long target) {
        return 두_역의_최소_시간_경로_조회를_요청(RestAssured.given(), source, target);
    }
}
