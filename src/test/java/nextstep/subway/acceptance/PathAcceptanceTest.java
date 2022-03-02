package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.EMAIL;
import static nextstep.subway.acceptance.MemberSteps.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회;
import static nextstep.subway.acceptance.PathSteps.경로_조회_실패;
import static nextstep.subway.acceptance.PathSteps.청소년_할인_요금_검증_됨;
import static nextstep.subway.acceptance.PathSteps.최단_거리_검증_됨;
import static nextstep.subway.acceptance.PathSteps.최단_거리_조회_됨;
import static nextstep.subway.acceptance.PathSteps.최단_거리의_시간_검증_됨;
import static nextstep.subway.acceptance.PathSteps.최단_시간_검증_됨;
import static nextstep.subway.acceptance.PathSteps.최단_시간_조회_됨;
import static nextstep.subway.acceptance.PathSteps.최단_시간의_거리_검증_됨;
import static nextstep.subway.acceptance.PathSteps.혜택_없는_요금_검증_됨_10km_이내;
import static nextstep.subway.acceptance.PathSteps.혜택_없는_요금_검증_됨_10km_초과;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10m, 3분)* ---     강남역
     * |                                    |
     * 3호선(2m, 10분)                       신분당선(10m, 2분)
     * |                                    |
     * 남부터미널역  --- *3호선(3m, 11분)* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(createLineCreateParams("2호선", "green", 교대역, 강남역, 10, 3))
                .jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(createLineCreateParams("신분당선", "red", 강남역, 양재역, 10, 2))
                .jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 2, 10))
                .jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 11));
    }

    /**
     * When 출발역과 도착역을 지정하여 조회한다.
     * Then 최단 경로에 포함된 역 정보와 거리를 반환한다.
     */
    @DisplayName("최단 경로 조회")
    @Test
    void getPath() {
        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 교대역, 양재역, PathType.DISTANCE);

        // then
        최단_거리_조회_됨(response, 교대역, 남부터미널역, 양재역);
        최단_거리_검증_됨(response, 5);
        최단_거리의_시간_검증_됨(response, 21);
    }

    /**
     * Given 조회할 경로의 출발점, 도착점을 지정한다.
     * When 출발역과 도착역을 같은 역으로 지정하여 조회한다.
     * Then 경로 조회가 실패한다.
     */
    @DisplayName("출발역과 도착역이 같은 경우 조회할 수 없다.")
    @Test
    void exceptionStartAndEndStationDuplication() {
        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 교대역, 교대역, PathType.DISTANCE);

        // then
        경로_조회_실패(response);
    }

    /**
     * Given 조회할 경로의 출발점, 도착점을 지정한다.
     * When 출발역과 도착역이 연결되어 있지 않은 역일 지정하여 조회힌다.
     * Then 경로 조회가 실패한다.
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 조회할 수 없다.")
    @Test
    void exceptionStartAndEndStationNoneConnection() {
        // given
        Long 가양역 = 지하철역_생성_요청("가양역").jsonPath().getLong("id");
        Long 증미역 = 지하철역_생성_요청("증미역").jsonPath().getLong("id");

        Long 연결_되어있지_않은_역 = 교대역;

        지하철_노선_생성_요청(createLineCreateParams("9호선", "brown", 가양역, 증미역, 10, 3));

        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 가양역, 연결_되어있지_않은_역, PathType.DISTANCE);

        // then
        경로_조회_실패(response);
    }

    /**
     * Given 조회할 경로의 출발점, 도착점을 지정한다.
     * When 존재하지 않는 역을 조회한다.
     * Then 경로 조회가 실패한다.
     */
    @DisplayName("존재하지 않는 역을 조회할 경우 경로 조회할 수 없다.")
    @Test
    void exceptionStartAndEndNotExistsStation() {
        // given
        Long 존재하지_않는_출발역 = -1L;
        Long 존재하지_않는_도착역 = -9_999L;

        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 존재하지_않는_출발역, 존재하지_않는_도착역, PathType.DISTANCE);

        // then
        경로_조회_실패(response);
    }

    /**
     * When 출발역과 도착역을 지정하여 조회한다.
     * Then 최소 시간 경로에 포함된 역 정보와 거리를 반환한다.
     */
    @DisplayName("최소 시간 기준 경로 조회")
    @Test
    void getDurationPath() {
        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
    }

    /**
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답
     */
    @DisplayName("최단 경로 조회 -> 10km 이내")
    @Test
    void getPath1() {
        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 교대역, 양재역, PathType.DISTANCE);

        // then
        최단_거리_조회_됨(response, 교대역, 남부터미널역, 양재역);
        최단_거리_검증_됨(response, 5);
        최단_거리의_시간_검증_됨(response, 21);
        혜택_없는_요금_검증_됨_10km_이내(response, 1_250);
    }

    /**
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답
     */
    @DisplayName("최단 경로 조회 -> 10km 초과")
    @Test
    void getPath2() {
        // when
        ExtractableResponse<Response> response = 경로_조회(this.given(), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
        혜택_없는_요금_검증_됨_10km_초과(response, 1_450);
    }

    /**
     * Given 청소년 로그인되어 있음.
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답(청소년 할인된 요금, 최대 경계값)
     */
    @DisplayName("로그인한 사용자 경로 조회 - 청소년은 350원 공제 후 20% 할인, 18세 최대 경계값")
    @Test
    void getPathLoginTeenagerMaxAge() {
        // given
        int teenagerMaxAge = 18;
        회원_생성_요청(EMAIL, PASSWORD, teenagerMaxAge);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 경로_조회(this.givenLogin(accessToken), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
        청소년_할인_요금_검증_됨(response, 880);
    }

    /**
     * Given 청소년 로그인되어 있음.
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답(청소년 할인된 요금, 최소 경계값)
     */
    @DisplayName("로그인한 사용자 경로 조회 - 청소년은 350원 공제 후 20% 할인, 13세 최소 경계값")
    @Test
    void getPathLoginTeenagerMinAge() {
        // given
        int teenagerMinAge = 13;
        회원_생성_요청(EMAIL, PASSWORD, teenagerMinAge);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 경로_조회(this.givenLogin(accessToken), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
        청소년_할인_요금_검증_됨(response, 880);
    }

    /**
     * Given 어린이 로그인되어 있음.
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답(어린이 할인된 요금, 최대 경계값)
     */
    @DisplayName("로그인한 사용자 경로 조회 - 어린이는 350원 공제 후 50% 할인, 12세 최대 경계값")
    @Test
    void getPathLoginChildMaxAge() {
        // given
        int teenagerMaxAge = 12;
        회원_생성_요청(EMAIL, PASSWORD, teenagerMaxAge);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 경로_조회(this.givenLogin(accessToken), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
        청소년_할인_요금_검증_됨(response, 550);
    }

    /**
     * Given 어린이 로그인되어 있음.
     * When 출발역과 도착역까지의 최단 거리 경로 조회 요청
     * Then 최단 거리 경로 응답
     * AND 총 거리와 소요 시간 응답
     * AND 지하철 이용 요금도 포함하여 응답(어린이 할인된 요금, 최소 경계값)
     */
    @DisplayName("로그인한 사용자 경로 조회 - 어린이는 350원 공제 후 50% 할인, 6세 최소 경계값")
    @Test
    void getPathLoginChildMinAge() {
        // given
        int teenagerMinAge = 13;
        회원_생성_요청(EMAIL, PASSWORD, teenagerMinAge);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 경로_조회(this.givenLogin(accessToken), 교대역, 양재역, PathType.DURATION);

        // then
        최단_시간_조회_됨(response, 교대역, 강남역, 양재역);
        최단_시간_검증_됨(response, 5);
        최단_시간의_거리_검증_됨(response, 20);
        청소년_할인_요금_검증_됨(response, 880);
    }

    private Map<String, String> createLineCreateParams(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", String.valueOf(upStationId));
        lineCreateParams.put("downStationId", String.valueOf(downStationId));
        lineCreateParams.put("distance", String.valueOf(distance));
        lineCreateParams.put("duration", String.valueOf(duration));
        return lineCreateParams;
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration", String.valueOf(duration));
        return params;
    }

    private RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    private RequestSpecification givenLogin(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken);
    }
}
