package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.PathStep.*;
import static nextstep.subway.acceptance.StationSteps.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        이호선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("2호선", "green", 교대역, 강남역, 100, 2)
        );
        신분당선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("신분당선", "red", 강남역, 양재역, 100, 3)
        );
        삼호선 = 지하철_노선_생성_요청_하고_ID_반환(
            createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 2, 100)
        );

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 100));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        경로_조회_성공(
            두_역의_최단_거리_경로_조회를_요청(교대역, 양재역),
            5, 200,
            교대역, 남부터미널역, 양재역
        );
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        경로_조회_성공(
            두_역의_최소_시간_경로_조회를_요청(교대역, 양재역),
            200, 5,
            교대역, 강남역, 양재역
        );
    }
}
