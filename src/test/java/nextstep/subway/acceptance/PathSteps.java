package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {
    public static ExtractableResponse<Response> 경로_조회(RequestSpecification given, Long source, Long target, PathType type) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    static void 경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    static void 최단_시간_조회_됨(ExtractableResponse<Response> response, Long 교대역, Long 강남역, Long 양재역) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> stations = response.jsonPath().getList("stations.id", Long.class);
        assertThat(stations).containsExactly(교대역, 강남역, 양재역);
    }

    static void 최단_거리_조회_됨(ExtractableResponse<Response> response, Long 교대역, Long 강남역, Long 양재역) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> stations = response.jsonPath().getList("stations.id", Long.class);
        assertThat(stations).containsExactly(교대역, 강남역, 양재역);
    }

    static void 최단_거리_검증_됨(ExtractableResponse<Response> response, int expectedDistance) {
        int distance = response.jsonPath().getInt("distance");
        assertThat(distance).isEqualTo(expectedDistance);
    }

    static void 최단_거리의_시간_검증_됨(ExtractableResponse<Response> response, int expectedDuration) {
        int duration = response.jsonPath().getInt("duration");
        assertThat(duration).isEqualTo(expectedDuration);
    }

    static void 최단_시간_검증_됨(ExtractableResponse<Response> response, int expectedDuration) {
        int duration = response.jsonPath().getInt("duration");
        assertThat(duration).isEqualTo(expectedDuration);
    }

    static void 최단_시간의_거리_검증_됨(ExtractableResponse<Response> response, int expectedDistance) {
        int distance = response.jsonPath().getInt("distance");
        assertThat(distance).isEqualTo(expectedDistance);
    }

    static void 혜택_없는_요금_검증_됨_10km_이내(ExtractableResponse<Response> response, int expectedFare) {
        int fare = response.jsonPath().getInt("fare");
        assertThat(fare).isEqualTo(expectedFare);
    }

    static void 혜택_없는_요금_검증_됨_10km_초과(ExtractableResponse<Response> response, int expectedFare) {
        int fare = response.jsonPath().getInt("fare");
        assertThat(fare).isEqualTo(expectedFare);
    }

    static void 청소년_할인_요금_검증_됨(ExtractableResponse<Response> response, int expectedFare) {
        int fare = response.jsonPath().getInt("fare");
        assertThat(fare).isEqualTo(expectedFare);
    }
}

