package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public final static String DISTANCE_TYPE = "DISTANCE";
    public final static String DURATION_TYPE = "DURATION";

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(final Long source,
                                                                     final Long target) {
        return 두_역의_경로_조회_요청(source, target, DISTANCE_TYPE);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(final Long source,
                                                                     final Long target) {
        return 두_역의_경로_조회_요청(source, target, DURATION_TYPE);
    }

    private static ExtractableResponse<Response> 두_역의_경로_조회_요청(final Long source,
                                                               final Long target,
                                                               final String type) {
        return 두_역의_경로_조회_요청(
                source,
                target,
                type,
                RestAssured.given().log().all()
        );
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회_요청(final Long source,
                                                               final Long target,
                                                               final String type,
                                                               final RequestSpecification spec) {
        return spec
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_두_역의_최소_시간_경로_조회를_요청(final String accessToken,
                                                                             final Long source,
                                                                             final Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", DURATION_TYPE)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_조회_검증(final ExtractableResponse<Response> response,
                         final int expectedDistance,
                         final int expectedDuration,
                         final int expectedFare,
                         final Long... expectedStationId) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(expectedStationId);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(expectedDistance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(expectedDuration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(expectedFare);
    }
}
