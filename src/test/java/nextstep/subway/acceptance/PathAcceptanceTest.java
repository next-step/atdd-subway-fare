package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
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
    private Long 부산역;
    private Long 기차;

    /**
     * 교대역   ---  10/4  ---  강남역
     * |                        |
     * 5/5                     10/2
     * |                        |
     * 남부터미널역  --- 3/10 ---  양재
     * |
     * 46/10
     * |
     * 부산역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        부산역 = 지하철역_생성_요청(관리자, "부산역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 300, 10, 4);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 200, 10, 2);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 100, 5, 5);
        기차 = 지하철_노선_생성_요청("기차", "orange", 교대역, 남부터미널역, 900, 5, 5);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
        지하철_노선에_지하철_구간_생성_요청(관리자, 기차, createSectionCreateParams(남부터미널역, 부산역, 46, 10));
    }

    /**
     * - Scenario: 두 역의 최소 시간 경로 조회
     * - When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * - Then 최소 시간 기준 경로를 응답
     * - And 총 거리와 소요 시간을 함께 응답함
     * - And 지하철 이용 요금도 함께 응답함
     */
    @Test
    @DisplayName("두 역의 최소 소요 시간 거리를 조회한다.")
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getString("duration")).isEqualTo("6");
        assertThat(response.jsonPath().getString("distance")).isEqualTo("20");
        assertThat(response.jsonPath().getString("fare")).isEqualTo("1750");
    }


    /**
     * - Scenario: 두 역의 최단 거리 경로 조회
     * - When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * - Then 최단 거리 기준 경로를 응답
     * - And 총 거리와 소요 시간을 함께 응답함
     * - And 지하철 이용 요금도 함께 응답함
     */
    @Test
    @DisplayName("교대역에서 부산역까지 거리는 51이고 요금은 기본 요금인 2150과 추가 운임 비용인 900원을 합친 3050원이다.")
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_조회를_요청(교대역, 부산역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 부산역);
        assertThat(response.jsonPath().getString("duration")).isEqualTo("15");
        assertThat(response.jsonPath().getString("distance")).isEqualTo("51");
        assertThat(response.jsonPath().getString("fare")).isEqualTo("3050");
    }

    /**
     * - When 청소년이 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * - Then 최단 거리 기준 경로를 응답하고
     * - And 총 거리와 소요 시간을 함께 응답하게 된다.
     * - And 지하철 이용 요금도 함께 응답하게 된다.
     */
    @Test
    @DisplayName("교대역에서 부산역까지 거리는 51이고 청소년의 요금은 2760원이다..")
    void findPathByDistance_Youth() {

        // when
        String accessToken = 로그인_되어_있음("youth@email.com", "password");
        ExtractableResponse<Response> 청소년_최단_거리_조회 = 인증_사용자_두_역의_최단_거리_조회를_요청(accessToken, 교대역, 부산역);

        // then
        assertThat(청소년_최단_거리_조회.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 부산역);
        assertThat(청소년_최단_거리_조회.jsonPath().getString("duration")).isEqualTo("15");
        assertThat(청소년_최단_거리_조회.jsonPath().getString("distance")).isEqualTo("51");
        assertThat(청소년_최단_거리_조회.jsonPath().getString("fare")).isEqualTo("2510");
    }


    /**
     * When 교대역에서 양재로 갈 때
     * Then 최단 거리로 가는 방법은 교대역-남부역-양재역으로 가는 방법이고
     * Then 최소 소요 시간으로 가는 방법은 교대역-강남역-양재역으로 가는 방법이다.
     */
    @Test
    void findPath() {
        ExtractableResponse<Response> 최소_소요_시간 = 두_역의_최소_소요_시간_조회를_요청(교대역, 양재역);
        assertThat(최소_소요_시간.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(최소_소요_시간.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);

        ExtractableResponse<Response> 최단_거리 = 두_역의_최단_거리_조회를_요청(교대역, 양재역);
        assertThat(최단_거리.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(최단_거리.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }


    private ExtractableResponse<Response> 인증_사용자_두_역의_최단_거리_조회를_요청(String accessToken, Long source, Long target) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths/distance?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
        return response;
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths/distance?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최소_소요_시간_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths/duration?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int additionalCharge, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("additionalCharge", additionalCharge + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
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
