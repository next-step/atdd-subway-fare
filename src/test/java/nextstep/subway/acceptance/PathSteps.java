package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathCondition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static void 경로의_전체_요금_확인(ExtractableResponse<Response> response, int price) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(price);
    }

    public static void 경로의_전체_시간_확인(ExtractableResponse<Response> response, int duration) {
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }

    public static void 경로의_전체_거리_확인(ExtractableResponse<Response> response, int distance) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
    }

    public static void 경로의_역_목록_확인(ExtractableResponse<Response> response, Long... ids) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(ids);
    }

    public static void 경로_조회_응답_확인(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
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
