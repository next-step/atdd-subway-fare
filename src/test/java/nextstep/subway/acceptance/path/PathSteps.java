package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(RequestSpecification given, Long source, Long target) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(Map.of(
                        "source", source,
                        "target", target,
                        "type", PathType.DISTANCE.name()
                ))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(RequestSpecification given, Long source, Long target) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(Map.of(
                        "source", source,
                        "target", target,
                        "type", PathType.DURATION.name()
                ))
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_조회_응답_검증(ExtractableResponse<Response> response, int distance, int duration, int fare, Long... stations) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
    }
}
