package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class PathSteps extends AcceptanceTestSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DURATION);
    }

    public static ExtractableResponse<Response> 두_역의_최단_경로_요청(Long source, Long target, PathType pathType) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("pathType", pathType.name())
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 로그인_두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        return 로그인_두_역의_최단_경로_요청(source, target, PathType.DISTANCE, accessToken);
    }

    public static ExtractableResponse<Response> 로그인_두_역의_최단_시간_경로_조회를_요청(Long source, Long target, String accessToken) {
        return 로그인_두_역의_최단_경로_요청(source, target, PathType.DURATION, accessToken);
    }

    public static ExtractableResponse<Response> 로그인_두_역의_최단_경로_요청(Long source, Long target, PathType pathType
                                , String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("pathType", pathType.name())
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all().extract();
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
