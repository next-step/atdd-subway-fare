package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAssertionUtils {

    public static void 최적경로의_역_아이디_목록의_순서는_다음과_같다(final ExtractableResponse<Response> response, final Long... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
    }
    public static  void 최적경로의_거리는_다음과_같다(final ExtractableResponse<Response> response, final int distance) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
    }
    public static  void 최적경로의_소요시간은_다음과_같다(final ExtractableResponse<Response> response, final int duration) {
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }
    public static  void 최적경로의_요금은_다음과_같다(final ExtractableResponse<Response> response, final int fare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }
}
