package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.domain.fare.MemberDiscountPolicy.CHILD;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 판교역;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * * 노선(거리, 시간, 요금)
     *
     * 교대역       ---- *2호선(10, 3, 0)*  ---- 강남역
     * |                                   |
     * *3호선(2, 10, 300)*                  *신분당선(10, 3, 100)*
     * |                                   |
     * 남부터미널역  ---- *3호선(3, 10, 300)* ----  양재역
     *                                     |
     *                                     *신분당선(40, 20, 100)*
     *                                     |
     *                                   ----  판교역
     *
     * * (거리, 시간) [경로]
     * * (총 요금, 기본 요금, 노선 추가 금액, 거리 추가 금액, 사용자 할인 금액)
     * * 교대역 > 판교역
     * 최단 거리: (45, 40) [교대역, 남부터미널역, 양재역, 판교역]
     * 요    금: 어 른(2250, 1250, 300, 700, 0)
     *        : 청소년(1870, 1250, 300, 700, 380)
     *        : 어린이(1300, 1250, 300, 700, 950)
     *
     * 최소 시간: (60, 26) [교대역, 강남역, 양재역, 판교역]
     * 요    금: 어 른(2050, 1250, 100, 700, 0)
     *        : 청소년(1710, 1250, 100, 700, 340)
     *        : 어린이(1200, 1250, 100, 700, 850)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 3);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3, 100);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 300);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(양재역, 판교역, 40, 20));
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최단 거리 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     And 로그인 되어있음
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, CHILD.getAge());
        회원_생성됨(createResponse);
        String 사용자 = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 판교역, 사용자);

        // then
        두_역의_최단_거리_경로_조회_완료(
                response, 45, 40,
                1300, 1250, 300, 700, 950,
                교대역, 남부터미널역, 양재역, 판교역);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최소 시간 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *     Then 최소 시간 기준 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 판교역);

        // then
        두_역의_최소_시간_경로_조회_완료(
                response, 60, 26,
                2050, 1250, 100, 700, 0,
                교대역, 강남역, 양재역, 판교역);
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
