package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

    private static final String 소요시간 = "DURATION";
    private static final String 거리 = "DISTANCE";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private final int 교대역_강남역_거리 = 10;
    private final int 교대역_강남역_시간 = 10;
    private final int 강남역_양재역_거리 = 10;
    private final int 강남역_양재역_시간 = 3;
    private final int 교대역_남부터미널역_거리 = 2;
    private final int 교대역_남부터미널역_시간 = 10;
    private final int 남부터미널역_양재역_거리 = 3;
    private final int 남부터미널역_양재역_시간 = 10;

    /**
     * 교대역    --- *2호선*  ---   강남역
     *    |                        |
     * *3호선*                   *신분당선*
     *    |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 교대역_강남역_거리, 교대역_강남역_시간);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 강남역_양재역_거리, 강남역_양재역_시간);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 교대역_남부터미널역_거리, 교대역_남부터미널역_시간);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 남부터미널역_양재역_거리, 남부터미널역_양재역_시간));
    }


    /**
     *  총 거리 5 = 2 + 3
     *  요금 1,250
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        두_역의_최단_거리_경로_조회에_성공한다(
            response,
            List.of(교대역, 남부터미널역, 양재역),
            교대역_남부터미널역_거리 + 남부터미널역_양재역_거리,
            1_250
        );
    }

    /**
     *  총 거리 20 = 10 + 5 + 5
     *  요금 1,250 + 100 * 2
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        두_역의_최소_시간_경로_조회에_성공한다(
            response,
            List.of(교대역, 강남역, 양재역),
            교대역_강남역_시간 + 강남역_양재역_시간,
            1_450
        );
    }

    private void 두_역의_최단_거리_경로_조회에_성공한다(
        ExtractableResponse<Response> response,
        List<Long> 역들,
        int distance,
        int fare
    ) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsAll(역들),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare)
        );
    }

    private void 두_역의_최소_시간_경로_조회에_성공한다(
        ExtractableResponse<Response> response,
        List<Long> 역들,
        int duration,
        int fare
    ) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsAll(역들),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare)
        );
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, 거리);
    }

    private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_경로_조회를_요청(source, target, 소요시간);
    }

    private ExtractableResponse<Response> 두_역의_경로_조회를_요청(
        Long source,
        Long target,
        String shortestType
    ) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={shortestType}", source, target, shortestType)
            .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(
        String name,
        String color,
        Long upStation,
        Long downStation,
        int distance,
        int duration
    ) {
        Map<String, Object> lineCreateParams = Map.of(
            "name", name,
            "color", color,
            "upStationId", upStation,
            "downStationId", downStation,
            "distance", distance,
            "duration", duration
        );
        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
