package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private static final String DISTANCE = "DISTANCE";
    private static final String DURATION = "DURATION";

    private String 어린이;
    private String 청소년;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 잠실역;
    private Long 선릉역;
    private Long 논현역;
    private Long 신논현역;
    private Long 남부터미널역;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 사호선;
    private Long 구호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재 --- *4호선* --- 잠실역 --- *4호선* --- 선릉역
     *                                                                     |
     *                                                                   *9호선*
     *                                                                     |
     *                                                                    논현역
     *                                                                     |
     *                                                                   *9호선*
     *                                                                     |
     *                                                                   신논현역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        잠실역 = 지하철역_생성_요청(관리자, "잠실역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청(관리자, "잠실역").jsonPath().getLong("id");
        논현역 = 지하철역_생성_요청(관리자, "잠실역").jsonPath().getLong("id");
        신논현역 = 지하철역_생성_요청(관리자, "잠실역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 4, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4, 0);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5, 0);
        사호선 = 지하철_노선_생성_요청("4호선", "blue", 양재역, 선릉역, 50, 50, 0);
        구호선 = 지하철_노선_생성_요청("9호선", "gray", 선릉역, 논현역, 5, 5, 1200);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 사호선, createSectionCreateParams(잠실역, 선릉역, 20, 20));
        지하철_노선에_지하철_구간_생성_요청(관리자, 구호선, createSectionCreateParams(논현역, 신논현역, 3, 5));
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최소 시간 경로를 조회
     *     Given 지하철역이 등록되어있음
     *     And 지하철 노선이 등록되어있음
     *     And 지하철 노선에 지하철역이 등록되어있음
     *     When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     *     Then 최소 시간 기준 경로를 응답
     *     And 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. (10km 이하)")
    @Test
    void findPathByDistance() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 5, 10, 1250, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (10km 초과)")
    @Test
    void findPathByDistanceOver10km() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 잠실역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 35, 40, 1750, 교대역, 남부터미널역, 양재역, 잠실역);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (50km 초과)")
    @Test
    void findPathByDistanceOver50km() {
        var response = 두_역의_최단_경로_조회를_요청(교대역, 선릉역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 55, 60, 2150, 교대역, 남부터미널역, 양재역, 잠실역, 선릉역);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (청소년 탑승객)")
    @Test
    void findPathChildren() {
        어린이 = 로그인_되어_있음("children@email.com", "password");

        var response = 두_역의_최단_경로_조회를_요청(어린이, 교대역, 양재역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 5, 10, 530, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (어린이 탑승객)")
    @Test
    void findPathTeenager() {
        청소년 = 로그인_되어_있음("teenager@email.com", "password");

        var response = 두_역의_최단_경로_조회를_요청(청소년, 교대역, 양재역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 5, 10, 800, 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (추가 요금이 있는 노선을 지나는경우)")
    @Test
    void findPathSurchargeLine() {
        var response = 두_역의_최단_경로_조회를_요청(선릉역, 신논현역, DISTANCE);

        두_역의_최단_경로_정상적으로_조회됨(response, 8, 10, 2450, 선릉역, 논현역, 신논현역);
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
     */
    @DisplayName("두 역의 최단 소요시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_경로_조회를_요청(교대역, 양재역, DURATION);

        // then
        두_역의_최단_경로_정상적으로_조회됨(response, 20, 8, 1250, 교대역, 강남역, 양재역);
    }

    private ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_경로_조회를_요청(String token, Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private void 두_역의_최단_경로_정상적으로_조회됨(ExtractableResponse<Response> response,
                                      int distance, int duration, int fare, Long... stations) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surcharge) {
        Map<String, String> lineCreateParams = createLineParams(name, color, upStation, downStation, distance, duration);
        lineCreateParams.put("surcharge", surcharge + "");
        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createLineParams(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        return lineCreateParams;
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
