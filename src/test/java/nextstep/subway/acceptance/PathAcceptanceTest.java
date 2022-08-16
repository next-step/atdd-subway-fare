package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.domain.PathType.DISTANCE;
import static nextstep.subway.domain.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 고속터미널역;
    private Long 신사역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선*(10, 10) ---   강남역
     * |                        |
     * *3호선*(2,5)                   *신분당선* (10, 10)
     * |                        |
     * 남부터미널역  --- *3호선*(3, 5) ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        고속터미널역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        신사역 = 지하철역_생성_요청(관리자, "신사역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(고속터미널역, 교대역, 15, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(신사역, 고속터미널역, 46, 5));
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. 이동 거리 10km 이하")
    @Test
    void findPathByDistance_distance_under_10km() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 교대역, 양재역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10km~50km

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. 이동 거리 10 ~ 50km")
    @Test
    void findPathByDistance_distance_between_10km_50km() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 양재역, 고속터미널역, DISTANCE);
        // thenㅇ
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(양재역, 남부터미널역, 교대역, 고속터미널역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 50km이상

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. 이동 거리 50km 이상")
    @Test
    void findPathByDistance_distance_over_50km() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 양재역, 신사역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(양재역, 남부터미널역, 교대역, 고속터미널역, 신사역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(66);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
    }

    /*
        Scenario: 두 역의 최소 시간 경로를 조회
        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
        Then 최소 시간 기준 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
    */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {

        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 교대역, 양재역, DURATION);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
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
