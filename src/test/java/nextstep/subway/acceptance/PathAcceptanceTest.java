package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회_검증;
import static nextstep.subway.acceptance.PathSteps.노선_추가_요금_등록한다;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.세션_생성_파라미터_생성;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import java.util.List;
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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 20);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 세션_생성_파라미터_생성(남부터미널역, 양재역, 3, 2));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회_검증(response, List.of(교대역, 남부터미널역, 양재역), 5, 22, 100);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회_검증(response, List.of(교대역, 강남역, 양재역), 20, 15, 400);
    }

    /**
     * Given 노선을 생성하고
     * When 노선에 추가 요금을 등록하면
     * Then 경로 조회시 추가된 요금이 기본 요금에 합쳐서 조회된다
     */
    @DisplayName("노선에 추가 요금을 등록한다")
    @Test
    void addExtraFareToLine() {
        // when
        노선_추가_요금_등록한다(삼호선, 10);

        // then
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);
        경로_조회_검증(response, List.of(교대역, 남부터미널역, 양재역), 5, 22, 110);
    }

    /**
     * Given 노선을 생성하고
     * When 여러 노선에 추가 요금을 등록하면
     * Then 경로 조회시 추가된 요금중 가장 높은 금액만 기본 요금에 합쳐서 조회된다
     */
    @DisplayName("경로 조회시 추가된 요금중 가장 높은 금액만 기본 요금에 합쳐서 조회된다")
    @Test
    void testOnlyHighestAdditionalFareIsAddedToBaseFare() {
        // when
        노선_추가_요금_등록한다(이호선, 20);
        노선_추가_요금_등록한다(신분당선, 30);
        노선_추가_요금_등록한다(삼호선, 40);

        // then
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);
        경로_조회_검증(response, List.of(교대역, 강남역, 양재역), 20, 15, 430);
    }

    /**
     * Given 자하철 역을 등록하고
     * And 노선을 등록하고
     * And 구간을 등록하고
     * And 로그인하고
     * When 두 역의 최단 거리 경로를 조회하면
     * Then 할인이 적용된 요금으로 조회된다
     */
    @DisplayName("로그인 사용자의 경유 경로 조회시 연령별 요금으로 조회된다")
    @Test
    void testQueryTransitPathWithAgeBasedFareForLoggedInUser() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, 사용자_12세);

        // then
        경로_조회_검증(response, List.of(교대역, 남부터미널역, 양재역), 5, 22, 50);
    }
}
