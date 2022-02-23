package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int fare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("fare", fare + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathType pathType, RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("pathType", pathType)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathType pathType, RequestSpecification spec, String accessToken) {
        RequestSpecification oauthSpec = RestAssured.given(spec).auth().oauth2(accessToken);
        return 두_역의_경로_조회를_요청(source, target, PathType.DISTANCE, oauthSpec);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, PathType.DISTANCE, RestAssured.given());
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        return 두_역의_경로_조회를_요청(source, target, PathType.DISTANCE, RestAssured.given(), accessToken);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, PathType.DURATION, RestAssured.given());
    }

    public static void 두_역의_최단_거리_경로_조회_완료(ExtractableResponse<Response> response,
                                           int distance, int duration, int fare,
                                           int basicFare, int lineOverFare, int distanceOverFare, int memberDiscountFare,
                                           Long... stations) {
        두_역의_경로_조회_완료(response, distance, duration, fare, basicFare, lineOverFare, distanceOverFare, memberDiscountFare,stations);
    }

    public static void 두_역의_최소_시간_경로_조회_완료(ExtractableResponse<Response> response,
                                           int distance, int duration, int fare,
                                           int basicFare, int lineOverFare, int distanceOverFare, int memberDiscountFare,
                                           Long... stations) {
        두_역의_경로_조회_완료(response, distance, duration, fare, basicFare, lineOverFare, distanceOverFare, memberDiscountFare,stations);
    }

    private static void 두_역의_경로_조회_완료(ExtractableResponse<Response> response,
                                      int distance, int duration, int fare,
                                      int basicFare, int lineOverFare, int distanceOverFare, int memberDiscountFare,
                                      Long[] stations) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
        assertThat(response.jsonPath().getInt("basicFare")).isEqualTo(basicFare);
        assertThat(response.jsonPath().getInt("lineOverFare")).isEqualTo(lineOverFare);
        assertThat(response.jsonPath().getInt("distanceOverFare")).isEqualTo(distanceOverFare);
        assertThat(response.jsonPath().getInt("memberDiscountFare")).isEqualTo(memberDiscountFare);
    }
}
