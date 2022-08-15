package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {
    private static ExtractableResponse<Response> getRequest(Long source, Long target, String type, RequestSpecification given) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type) {
        return getRequest(source, target, type, given());
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(String token, Long source, Long target, String type) {
        return getRequest(source, target, type, given(token));
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type, RequestSpecification spec) {
        return getRequest(source, target, type, given(spec));
    }
}
