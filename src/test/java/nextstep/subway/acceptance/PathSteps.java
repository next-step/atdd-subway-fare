package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final RequestSpecification spec, Long source, Long target) {
        return getResponseExtractableResponse(spec, source, target, "DISTANCE");
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return getResponseExtractableResponse(RestAssured.given(), source, target, "DISTANCE");
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return getResponseExtractableResponse(source, target, "DURATION");
    }

    private static ExtractableResponse<Response> getResponseExtractableResponse(final Long source, final Long target, final String type) {
        return getResponseExtractableResponse(RestAssured.given(), source, target, type);
    }

    private static ExtractableResponse<Response> getResponseExtractableResponse(RequestSpecification requestSpecification, final Long source, final Long target, final String type) {
        return requestSpecification.log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
