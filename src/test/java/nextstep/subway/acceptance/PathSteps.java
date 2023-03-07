package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_최단_거리_또는_시간_경로_조회를_요청(RequestSpecification given, Long source, Long target, PathType pathType) {
        return given
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", pathType.name())
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> 최단_경로_응답, int expectedDistance, int expectedDuration, Long... expectedStationIds) {
        assertAll(
                () -> assertThat(최단_경로_응답.jsonPath().getList("stations.id", Long.class)).containsExactly(expectedStationIds),
                () -> assertThat(최단_경로_응답.jsonPath().getInt("distance")).isEqualTo(expectedDistance),
                () -> assertThat(최단_경로_응답.jsonPath().getInt("duration")).isEqualTo(expectedDuration)
        );
    }

    public static void 최단_경로의_거리에_대한_요금이_조회됨(ExtractableResponse<Response> 최단_경로_응답, int expectedFare) {
        assertThat(최단_경로_응답.jsonPath().getInt("fare")).isEqualTo(expectedFare);
    }
}
