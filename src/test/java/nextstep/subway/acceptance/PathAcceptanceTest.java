package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_로그인_성공_토큰_발급됨;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_또는_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.최단_경로_조회됨;
import static nextstep.subway.acceptance.PathSteps.최단_경로의_거리에_대한_요금이_조회됨;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private final String PASSWORD = "password";

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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 0, 교대역, 강남역, 10, 4);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 0, 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 300, 교대역, 남부터미널역, 2, 4);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 4));
    }

    /**
     * given 총 4개의 역, 3개의 노선이 등록된 상황에서
     * when 출발역, 도착역, 거리 기준 최단 거리 경로 조회를 요청하면
     * then 최단 거리 경로 역 리스트와 총 거리, 총 시간을 조회할 수 있다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_또는_시간_경로_조회를_요청(given(), 교대역, 양재역, PathType.DISTANCE);

        // then
        int expectedDistance = 5;
        int expectedDuration = 8;
        최단_경로_조회됨(response, expectedDistance, expectedDuration, 교대역, 남부터미널역, 양재역);
    }

    /**
     * given 총 4개의 역, 3개의 노선이 등록된 상황에서
     * when 출발역, 도착역, 시간 기준 최단 시간 경로 조회를 요청하면
     * then 최단 거리 경로 역 리스트와 총 거리, 총 시간을 조회할 수 있다.
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_또는_시간_경로_조회를_요청(given(), 교대역, 양재역, PathType.DURATION);

        // then
        int expectedDistance = 20;
        int expectedDuration = 7;
        최단_경로_조회됨(response, expectedDistance, expectedDuration, 교대역, 강남역, 양재역);
    }

    /**
     * given 총 4개의 역, 3개의 노선이 등록된 상황에서
     * when 출발역, 도착역, 시간 기준 최단 시간 & 최단 거리 경로 조회를 요청했을 때
     * then 각 최단 경로에 대한 총 요금을 계산할 수 있다.
     */
    @DisplayName("두 역의 최단 경로에 대한 총 요금을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "DURATION, 1450",
            "DISTANCE, 1250"
    })
    void findPathAndCalculateFare(PathType pathType, int expectedFare) {
        // when
        ExtractableResponse<Response> 교대_양재_최단_경로_응답 = 두_역의_최단_거리_또는_시간_경로_조회를_요청(given(), 교대역, 양재역, pathType);

        // then
        최단_경로의_거리에_대한_요금이_조회됨(교대_양재_최단_경로_응답, expectedFare);
    }

    /**
     * given 총 4개의 역, 3개의 노선, 연령별 사용자가 등록된 상황에서 (1개 노선은 추가요금이 부여됨)
     * when 연령별로 출발역, 도착역의 거리 기준으로 조회했을 때
     * then 노선의 추가 요금, 연령별 할인이 적용된 요금을 조회할 수 있다.
     */
    @DisplayName("두 역의 최단 경로 사이에 추가요금이 있는 노선과, 연령별 추가요금이 계산된 총 요금을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "child@email.com, 7, 600",
            "youth@email.com, 14, 960",
            "adult@email.com, 29, 1550"
    })
    void findPathAndCalculateFareWithAdditionalFarePolicy(String email, int age, int expectedFare) {
        // given
        회원_생성_요청(email, PASSWORD, age);
        String 사용자_토큰 = 베어러_로그인_성공_토큰_발급됨(email, PASSWORD);

        // when
        ExtractableResponse<Response> 교대_양재_최단_경로_응답 = 두_역의_최단_거리_또는_시간_경로_조회를_요청(given(사용자_토큰), 교대역, 양재역, PathType.DISTANCE);

        // then
        최단_경로의_거리에_대한_요금이_조회됨(교대_양재_최단_경로_응답, expectedFare);
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
