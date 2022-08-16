package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private static int BASE_FARE =  1250;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 2);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 11);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
    }

    /* when 출발지가 교대역이고 목적지가 양재역일 때 최단 거리 경로를 조회한다.
     * then 구간의 최단 경로 거리의 합을 검증한다.
     * and  경로가 "교대역" "남부터미널역" "양재역" 과 일치하는지 검증한다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        final Integer distance = 5;
        최단_거리_경로_조회_검증(response, distance, 교대역, 남부터미널역, 양재역);
    }


    /* when 출발지가 교대역이고 목적지가 양재역일 때 최단 소요시간 경로를 조회한다.
     * then 구간의 최단 소요시간의 합을 검증한다.
     * and  경로가 "교대역" "강남역" "양재역" 과 일치하는지 검증한다.
     */
    @DisplayName("두 역의 최단 소요시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        //when
        ExtractableResponse<Response> response = 두_역의_최단_소요시간_경로_조회를_요청(교대역, 양재역);

        //then
        final Integer duration = 4;
        최단_소요시간_경로_조회_검증(response, duration, 교대역, 강남역, 양재역);
    }

    /*
     * when 교대역과 양재역의 최단 거리 경로를 조회한다
     * then 구간의 최단 거리의 합을 검증한다.
     * and  경로가 "교대역" "남부터미널역" "양재역" 과 일치하는지 검증한다.
     * and  거리가 10을 초과하지 않기 때문에 운임요금이 BASE_FARE 와 동일한지 검증한다.
     */
    @DisplayName("두 역의 최단 거리 경로 운임 요금을 조회한다.")
    @Test
    void FindFaresOnThePathByDistance() {
        //when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        //then
        final Integer distance = 5;
        최단_거리_경로_운임요금_조회_검증(response, BASE_FARE, distance, 교대역, 남부터미널역, 양재역);
    }

    private void 최단_거리_경로_운임요금_조회_검증(ExtractableResponse<Response> response, int fare, Integer distance, Long ... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    private void 최단_소요시간_경로_조회_검증(ExtractableResponse<Response> response, Integer duration, Long ... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }

    private void 최단_거리_경로_조회_검증(ExtractableResponse<Response> response, Integer distance, Long ... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
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
