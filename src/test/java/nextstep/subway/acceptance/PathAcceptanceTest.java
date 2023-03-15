package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 선정릉역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 구호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---  양재  --- *9호선* --- 선정릉
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        선정릉역 = 지하철역_생성_요청("선정릉역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 8, 3);
        신분당선 = 추가요금_있는_지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 8, 4, 200);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 4, 3);
        구호선 = 추가요금_있는_지하철_노선_생성_요청("9호선", "brown", 양재역, 선정릉역, 5, 3, 500);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 8, 5));
    }

    /**
     * When 두 역의 최단거리 경로 조회를 요청하면
     * Then 최단거리 기준으로 경로를 응답받는다.
     * And 총 거리와 소요 시간을 함께 응답받는다.
     * And 지하철 이용 요금도 함께 응답받는다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회_검증(response, 12, 8, 1350, 교대역, 남부터미널역, 양재역);
    }

    /**
     * When 두 역의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답받는다.
     * And 총 거리와 소요 시간을 함께 응답받는다.
     * And 추가 요금이 적용된 지하철 이용 요금도 함께 응답받는다.
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회_검증(response, 16, 7, 1550, 교대역, 강남역, 양재역);
    }

    /**
     * When 추가 요금이 있는 노선 2개를 포함한 경로를 조회 요청하면
     * Then 가장 높은 금액의 추가 요금만 적용된 지하철 요금을 응답받는다.
     */
    @DisplayName("가장 높은 금액의 추가 요금만 적용한다.")
    @Test
    void findPathAndAddFare() {
        // when
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 선정릉역);

        // then
        경로_조회_검증(response, 21, 10, 1950, 교대역, 강남역, 양재역, 선정릉역);
    }

    /**
     * When 19세 이상 사용자가 경로 조회를 요청하면
     * Then 정상 요금을 응답받는다.
     */
    @Test
    @DisplayName("19세 이상 로그인 사용자는 정상 요금이 조회된다.")
    void findPathFareByMoreThan19() {
        // when
        var response = 로그인_사용자_두_역의_최소_시간_경로_조회를_요청(관리자, 교대역, 양재역);

        // then
        경로_조회_검증(response, 16, 7, 1550, 교대역, 강남역, 양재역);
    }

    /**
     * When 어린이(6세 이상 ~ 13세 미만) 사용자가 경로 조회를 요청하면
     * Then 350원을 공제한 금액의 50% 할인된 요금을 응답받는다.
     */
    @Test
    @DisplayName("어린이 사용자는 공제한 금액의 50% 할인된 요금이 조회된다.")
    void findPathFareByChildUser() {
        // when
        var response = 로그인_사용자_두_역의_최소_시간_경로_조회를_요청(어린이, 교대역, 선정릉역);

        // then
        경로_조회_검증(response, 21, 10, 1150, 교대역, 강남역, 양재역, 선정릉역);
    }

    /**
     * When 청소년(13세 이상 ~ 19세 미만) 사용자가 경로 조회를 요청하면
     * Then 350원을 공제한 금액의 20% 할인된 요금을 응답받는다.
     */
    @Test
    @DisplayName("청소년 사용자는 공제한 금액의 20% 할인된 요금이 조회된다.")
    void findPathFareByTeenagerUser() {
        // when
        var response = 로그인_사용자_두_역의_최소_시간_경로_조회를_요청(청소년, 교대역, 선정릉역);

        // then
        경로_조회_검증(response, 21, 10, 1630, 교대역, 강남역, 양재역, 선정릉역);
    }

    private Long 지하철_노선_생성_요청(final String name,
                              final String color,
                              final Long upStation,
                              final Long downStation,
                              final int distance,
                              final int duration) {

        return 추가요금_있는_지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    private Long 추가요금_있는_지하철_노선_생성_요청(final String name,
                                      final String color,
                                      final Long upStation,
                                      final Long downStation,
                                      final int distance,
                                      final int duration,
                                      final int addFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("addFare", addFare + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(final Long upStationId,
                                                          final Long downStationId,
                                                          final int distance,
                                                          final int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
