package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type=DISTANCE", source, target)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type=DURATION", source, target)
                .then().log().all().extract();
    }

    public static void 경로와_총_거리_총_소요시간이_조회됨(ExtractableResponse<Response> response, List<Long> stationIds, int distance, int duration) {
        assertAll(() -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactlyElementsOf(stationIds);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        });
    }
}
