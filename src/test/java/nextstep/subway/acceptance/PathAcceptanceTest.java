package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.AgeGroup;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathAcceptanceSteps.경로_전체_요금_조회됨;
import static nextstep.subway.acceptance.PathAcceptanceSteps.두_역의_경로_조회를_요청;
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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10, 300);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2, 1000);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    @ParameterizedTest(name = "두 역의 경로를 조회한다. [{arguments}]")
    @EnumSource(PathType.class)
    void findPath(PathType pathType) {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 교대역, 양재역, pathType);

        // then
        경로_역_목록_조회됨(response, 교대역, 남부터미널역, 양재역);
        경로_전체_거리_조회됨(response, 5);
        경로_전체_시간_조회됨(response, 5);
        경로_전체_요금_조회됨(response, 1250);
    }

    @ParameterizedTest(name = "로그인 상태에서 두 역의 경로를 조회한다. [{arguments}]")
    @MethodSource("findPathWithLogin")
    void findPathWithLogin(PathType pathType, String email, int age, int expected) {
        // when
        회원_생성_요청(email, PASSWORD, age);
        String 로그인_토큰 = 로그인_되어_있음(email, PASSWORD);
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(givenWithLogin(로그인_토큰), 교대역, 양재역, pathType);

        // then
        경로_역_목록_조회됨(response, 교대역, 남부터미널역, 양재역);
        경로_전체_거리_조회됨(response, 5);
        경로_전체_시간_조회됨(response, 5);
        경로_전체_요금_조회됨(response, expected);
    }

    private static Stream<Arguments> findPathWithLogin() {
        return Stream.of(
                Arguments.of(AgeGroup.CHILD, "child@email.com", 10, 450),
                Arguments.of(AgeGroup.YOUTH, "youth@email.com", 15, 720),
                Arguments.of(AgeGroup.ADULT, "adult@email.com", 20, 1250)
        );
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraCharge) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("extraCharge", extraCharge + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
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
