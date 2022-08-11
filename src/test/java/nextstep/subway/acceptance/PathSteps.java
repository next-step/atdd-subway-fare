package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathCondition;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, PathCondition.DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String accessToken, Long source, Long target) {
        return 두_역의_경로_조회를_요청(accessToken, source, target, PathCondition.DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, PathCondition.DURATION);
    }

    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(String accessToken, Long source, Long target) {
        return 두_역의_경로_조회를_요청(accessToken, source, target, PathCondition.DURATION);
    }

    private static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathCondition pathCondition) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("pathCondition", pathCondition.name())
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 두_역의_경로_조회를_요청(String accessToken, Long source, Long target, PathCondition pathCondition) {
        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("pathCondition", pathCondition.name())
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }
}
