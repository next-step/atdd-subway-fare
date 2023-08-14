package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.constant.FindPathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_및_소요시간_조회를_요청;
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

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 11);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 12);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 13));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_및_소요시간_조회를_요청(교대역, 양재역, FindPathType.DISTANCE.getType());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
    }

    /**
     * <pre>
     * Feature: 지하철 경로 검색
     *  Scenario: 두 역의 최소 시간 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *     Then 최소 시간 기준 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     * </pre>
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // given : 선행조건 기술

        // when : 기능 수행
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_및_소요시간_조회를_요청(교대역, 양재역, FindPathType.DURATION.getType());
        
        // then : 결과 확인
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(21);
    }

    /**
     * <pre>
     * Feature: 지하철 경로 검색
     *  Scenario: 두 역의 최단 거리 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     * </pre>
     */
    @DisplayName("두 역의 최소 거리 경로를 조회하고 이용 요금도 함께 응답한다.")
    @Test
    void findPathByDistanceReturnFare() {
        // given : 선행조건 기술

        // when : 기능 수행
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_및_소요시간_조회를_요청(교대역, 양재역, FindPathType.DISTANCE.getType());

        // then : 결과 확인
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
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
