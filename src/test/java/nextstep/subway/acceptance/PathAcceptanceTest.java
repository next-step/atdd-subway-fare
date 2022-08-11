package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로의_역_목록_확인;
import static nextstep.subway.acceptance.PathSteps.경로의_전체_거리_확인;
import static nextstep.subway.acceptance.PathSteps.경로의_전체_시간_확인;
import static nextstep.subway.acceptance.PathSteps.경로의_전체_요금_확인;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_시간_경로_조회를_요청;
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
     * 교대역    --- *2호선(10d, 2s)* ---   강남역
     * |                               |
     * *3호선(2d, 10s)*                  *신분당선(10d, 3s)*
     * |                               |
     * 남부터미널역  --- *3호선(3d, 5s)* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        /**
         * Given 지하철역이 등록되어있음
         * And 지하철 노선이 등록되어있음
         * And 지하철 노선에 지하철역이 등록되어있음
         */

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3, 1000);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 600);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        int extraFare = 600;

        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로의_역_목록_확인(response, 교대역, 남부터미널역, 양재역);
        경로의_전체_거리_확인(response, 5);
        경로의_전체_시간_확인(response, 15);
        경로의_전체_요금_확인(response, 1250 + extraFare);
    }

    /**
     * When 로그인을 한후
     * And 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("로그인 한 후 두 역의 최단 거리 경로를 조회한다.")
    @ParameterizedTest
    @MethodSource("user_info_distance")
    void findPathByDistanceWithLogin(String email, int age, int expected) {
        // given
        회원_생성_요청(email, PASSWORD, age);
        String 로그인_토큰 = 로그인_되어_있음(email, PASSWORD);
        int extraFare = 600;

        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(로그인_토큰, 교대역, 양재역);

        // then
        경로의_역_목록_확인(response, 교대역, 남부터미널역, 양재역);
        경로의_전체_거리_확인(response, 5);
        경로의_전체_시간_확인(response, 15);
        경로의_전체_요금_확인(response, expected);
    }

    private static Stream<Arguments> user_info_distance() {
        return Stream.of(
                Arguments.of("test1@email.com", 10, 1100),
                Arguments.of("test2@email.com", 13, 1550),
                Arguments.of("test3@email.com", 19, 1850)
        );
    }

    /**
     * When 출발역에서 도착역까지의 최단 시간 기준으로 경로 조회를 요청
     * Then 최단 시간 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByTime() {
        // given
        int extraFare = 1000;

        // when
        var response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로의_역_목록_확인(response, 교대역, 강남역, 양재역);
        경로의_전체_거리_확인(response, 20);
        경로의_전체_시간_확인(response, 5);
        경로의_전체_요금_확인(response, 1250 + extraFare);
    }

    /**
     * When 로그인을 한후
     * And 출발역에서 도착역까지의 최단 시간 기준으로 경로 조회를 요청
     * Then 최단 시간 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("로그인 한 후 두 역의 최단 시간 경로를 조회한다.")
    @ParameterizedTest
    @MethodSource("user_info_duration")
    void findPathByTimeWithLogin(String email, int age, int expected) {
        // given
        회원_생성_요청(email, PASSWORD, age);
        String 로그인_토큰 = 로그인_되어_있음(email, PASSWORD);
        int extraFare = 1000;

        // when
        var response = 두_역의_최단_시간_경로_조회를_요청(로그인_토큰, 교대역, 양재역);

        // then
        경로의_역_목록_확인(response, 교대역, 강남역, 양재역);
        경로의_전체_거리_확인(response, 20);
        경로의_전체_시간_확인(response, 5);
        경로의_전체_요금_확인(response, expected);
    }

    private static Stream<Arguments> user_info_duration() {
        return Stream.of(
                Arguments.of("test1@email.com", 10, 1800),
                Arguments.of("test2@email.com", 13, 2070),
                Arguments.of("test3@email.com", 19, 2250)
        );
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("extraFare", extraFare + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
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
