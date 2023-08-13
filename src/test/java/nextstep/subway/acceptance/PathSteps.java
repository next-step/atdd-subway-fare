package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.domain.FindPathType;
import org.springframework.http.MediaType;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long sourceId, Long targetId, FindPathType type, RequestSpecification req) {
        return req
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long sourceId, Long targetId, FindPathType type) {
        return RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
