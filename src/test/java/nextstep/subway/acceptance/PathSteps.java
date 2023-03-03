package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    private static final String 소요시간 = "DURATION";
    private static final String 거리 = "DISTANCE";

    private PathSteps() {
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        return 두_역의_경로_조회를_요청(source, target, 거리, accessToken);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target, String accessToken) {
        return 두_역의_경로_조회를_요청(source, target, 소요시간, accessToken);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, 거리);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, 소요시간);
    }

    private static ExtractableResponse<Response> 두_역의_경로_조회를_요청(
        Long source,
        Long target,
        String shortestType
    ) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={shortestType}", source, target, shortestType)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(
        RequestSpecification given,
        Long source,
        Long target,
        String shortestType
    ) {
        return given
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={shortestType}", source, target, shortestType)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(
        Long source,
        Long target,
        String shortestType,
        String accessToken
    ) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={shortestType}", source, target, shortestType)
            .then().log().all().extract();
    }
}
