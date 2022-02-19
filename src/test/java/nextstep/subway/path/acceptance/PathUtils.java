package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.acceptance.LineSteps;
import nextstep.subway.path.domain.PathType;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathUtils {

    private PathUtils() {
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, PathType type) {
        RequestSpecification requestSpecification = RestAssured.given().log().all();
        Map<String, Object> params = 경로_조회_요청_파라미터(source, target, type);

        return 두_역의_경로_조회를_요청(requestSpecification, params);
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(RequestSpecification requestSpecification, Map<String, Object> params) {
        return requestSpecification.accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static Map<String, Object> 경로_조회_요청_파라미터(Long source, Long target, PathType type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("type", type.name());

        return params;
    }

    public static void 경로_조회_성공(ExtractableResponse<Response> response, Iterable<Long> stations) {
        assertAll(() -> {
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactlyElementsOf(stations);
            assertThat(response.jsonPath().getInt("distance")).isEqualTo(66);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(66);
            assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
        });
         /*
            어느 리뷰어님의 리뷰를 보니 하나의 테스트에는 하나의 검증이 있는 것을 추천한다면서
            여러 검증이 필요한 경우 assertAll을 사용하라고 추천했습니다.
            제가 생각하기에는 assertAll을 써도 검증하는 것이 3개인데 차이점이 궁금합니다 :)
         */
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
