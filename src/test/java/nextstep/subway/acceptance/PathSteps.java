package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final Long source, final Long target) {
        return 두_역의_최적_경로_조회를_요청(source, target, PathType.DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(final Long source, final Long target) {
        return 두_역의_최적_경로_조회를_요청(source, target, PathType.DURATION);
    }

    public static ExtractableResponse<Response> 두_역의_최적_경로_조회를_요청(final Long source, final Long target, PathType pathType) {
        return RestAssured
                .given()
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .queryParam("type", pathType)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final String token, final Long source, final Long target) {
        return RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .queryParam("type", PathType.DISTANCE)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청_문서화(final RequestSpecification spec, final String accessToken, final Long source, final Long target, PathType pathType) {
        return RestAssured
                .given(spec)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .accept(APPLICATION_JSON_VALUE)
                    .queryParam("source", source)
                    .queryParam("target", target)
                    .queryParam("type", pathType)
                .when()
                    .get("/paths")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_추가_요금_추가_요청(final Long lineId, final int fare) {
        Map<String, String> param = new HashMap<>();
        param.put("fare", fare + "");

        return RestAssured
                .given()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(param)
                .when()
                    .post("/lines/{lineId}/fare", lineId)
                .then()
                    .log().all()
                .extract();
    }

}
