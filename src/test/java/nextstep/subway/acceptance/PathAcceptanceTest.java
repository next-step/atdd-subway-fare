package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 판교역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                          |
     *                          |
     *                          판교
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 10);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 판교역, 50, 100);
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회 요청
     * Then 최단 거리 기준 경로 및 총 거리 + 소요 시간 응답
     */
    @DisplayName("최단 거리 기준 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    /**
     * When 출발역에서 도착역까지의 최단 시간 기준으로 경로 조회 요청
     * Then 최단 시간 기준 경로 및 총 거리 + 소요 시간 응답
     */
    @DisplayName("최단 시간 기준 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(9);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450);
    }

    @DisplayName("거리 경로가 10km 이내일 경우, 기본운임 1,250원이 부과된다.")
    @Test
    void calculateBaseFareCase() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(양재역, 교대역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("거리 경로가 10초과 및 50km 이하일 경우, 기본운임에 5km 당 100원의 요금이 추과 부과된다.")
    @Test
    void calculateMiddleRangeFareCase() {
        // when
        var response = 두_역의_최단_시간_경로_조회를_요청(양재역, 교대역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450);
    }

    @DisplayName("거리 경로가 50km 초과일 경우, 기본운임에 8km 당 100원의 요금이 추과 부과된다.")
    @Test
    void calculateLongRangeFareCase() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(양재역, 판교역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1850);
    }
}
