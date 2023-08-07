package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_거리_경로_조회를_요청(RestAssured.given().log().all(), source, target);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String accessToken, Long source, Long target) {
        return 두_역의_최단_거리_경로_조회를_요청(RestAssured.given().log().all().auth().oauth2(accessToken), source, target);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification requestSpecification,
                                                                     Long source,
                                                                     Long target) {
        return requestSpecification
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, "DISTANCE")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, "DURATION")
                .then().log().all().extract();
    }

    public static void 경로_조회됨(ExtractableResponse<Response> response, List<Long> stationIds,
                              int distance, int duration, int fee) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds.toArray(Long[]::new));
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fee")).isEqualTo(fee);
    }
}
