package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final Long source, final Long target) {
        return RestAssured
                .given()
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final RequestSpecification spec, final Long source, final Long target) {
        return RestAssured
                .given(spec)
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

}
