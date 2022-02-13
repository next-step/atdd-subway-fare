package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class PathAcceptanceSteps {

    public static final String PATH_URI = "/paths";

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathType pathType) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("source", source);
        parameters.put("target", target);
        parameters.put("pathType", pathType.name());

        RequestSpecification given = givenWithJsonParameters(parameters);
        Response when = when(given);
        return then(when);
    }

    public static RequestSpecification given() {
        return RestAssured
                .given().log().all();
    }

    public static RequestSpecification givenWithJsonParameters(RequestSpecification requestSpecification,
                                                               Map<String, Object> parameters) {
        return requestSpecification
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(parameters);
    }

    public static RequestSpecification givenWithJsonParameters(Map<String, Object> parameters) {
        return givenWithJsonParameters(given(), parameters);
    }

    public static Response when(RequestSpecification requestSpecification) {
        return requestSpecification
                .when()
                .get(PATH_URI);
    }

    public static ExtractableResponse<Response> then(Response response) {
        return response
                .then()
                .log()
                .all()
                .extract();
    }

}
