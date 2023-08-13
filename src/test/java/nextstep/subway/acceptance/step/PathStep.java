package nextstep.subway.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;

public class PathStep {
    private PathStep() {
    }

    public static ExtractableResponse<Response> 경로_조회_요청(int sourceStationId, int targetStationId, String type, RequestSpecification requestSpecification) {
        Map<String, Object> params = Map.of(
                "source", sourceStationId,
                "target", targetStationId,
                "type", type
        );

        return requestSpecification
                .queryParams(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 역_이름_목록_검증(ExtractableResponse<Response> pathsResponse, int expectedSize, String... expectedStationNames) {
        List<String> stationNames = 역_이름_목록_추출(pathsResponse);

        assertThat(stationNames).hasSize(expectedSize);
        assertThat(stationNames).containsExactly(expectedStationNames);
    }

    private static List<String> 역_이름_목록_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getList("stations.name", String.class);
    }

    public static void 경로_응답_검증(ExtractableResponse<Response> pathsResponse, int expectedDistance, int expectedDuration, int expectedFare) {
        int distance = 총_이동거리_추출(pathsResponse);
        int duration = 소요시간_추출(pathsResponse);
        int fare = 이용_요금_추출(pathsResponse);

        assertThat(distance).isEqualTo(expectedDistance);
        assertThat(duration).isEqualTo(expectedDuration);
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static int 총_이동거리_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("distance");
    }

    private static int 소요시간_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("duration");
    }

    private static int 이용_요금_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("fare");
    }

}
