package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

public class PathSteps {
    public static final String DISTANCE = "DISTANCE";
    public static final String DURATION = "DURATION";

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 경로_찾기_문서화(RestAssured.given(), null, source, target, DISTANCE);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(final Long source, final Long target) {
        return 경로_찾기_문서화(RestAssured.given(), null, source, target, DURATION);
    }

    public static ExtractableResponse<Response> 경로_찾기_문서화(
            final RequestSpecification spec,
            final RestDocumentationFilter document,
            final Long source,
            final Long target,
            final String type
    ) {
        RequestSpecification given = RestAssured
                .given(spec).log().all();

        if (document != null) {
            given = given.filter(document);
        }

        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 두_역의_경로_조회_검증(final ExtractableResponse<Response> response, final Long... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }

    public static void 두_역의_최소_시간_경로_조회를_검증(
            final ExtractableResponse<Response> response,
            final Long distance,
            final Long duration
    ) {
        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance),
                () -> assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration)
        );
    }

    public static void 정상_요청이_아닐_경우_예외_처리한다(final ExtractableResponse<Response> findByDistanceResponse) {
        org.assertj.core.api.Assertions.assertThat(findByDistanceResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
