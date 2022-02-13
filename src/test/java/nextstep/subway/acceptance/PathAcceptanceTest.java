package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

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
     * 교대역    --- *2호선*(10m, 5min) ---    강남역
     * |                                      |
     * *3호선*                              *신분당선*
     * (2m, 10min)                       (10m, 5min)
     * |                                     |
     * 남부터미널역  --- *3호선*(3m, 10min) ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 구간_파라미터_생성(남부터미널역, 양재역, 3, 10));
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * Then 최단 거리 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회됨(response, 교대역, 남부터미널역, 양재역);
        경로_거리_조회됨(response, 5);
        경로_소요시간_조회됨(response, 20);
        지하철_요금_조회됨(response, 1250);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 거리 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회됨(response, 교대역, 강남역, 양재역);
        경로_거리_조회됨(response, 20);
        경로_소요시간_조회됨(response, 10);
        지하철_요금_조회됨(response, 1450);
    }

}
