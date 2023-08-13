package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class PathSteps {
    public static ExtractableResponse<Response> 두개_역_사이_경로_요청(Long sourceId, Long targetId, RequestSpecification req) {
        return req
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
