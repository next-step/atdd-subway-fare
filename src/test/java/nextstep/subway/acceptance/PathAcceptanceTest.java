package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.domain.path.PathType.DISTANCE;
import static nextstep.subway.domain.path.PathType.DURATION;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 고속터미널역;
    private Long 신사역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;


    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        고속터미널역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        신사역 = 지하철역_생성_요청(관리자, "신사역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10, 500);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5, 900);
        /**
         * 신사역
         * | (46km, 5분)
         * 고속터미널역
         * |
         * 교대역    --- *2호선*(10km, 10분) ---   강남역
         * |                                       |
         * *3호선*(2km,5분) (추가요금 900원)    *신분당선* (10km, 10분) (추가요금 500원)
         * |                                        |
         * 남부터미널역  --- *3호선*(3km, 5분) (추가요금 900원) ---   양재
         */
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(고속터미널역, 교대역, 15, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(신사역, 고속터미널역, 46, 5));
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
        And 지하철 기본 요금 1250원 + 3호선 추가 요금 900원 = 2150원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. 이동 거리 10km 이하")
    @Test
    void findPathByDistance_distance_under_10km() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 교대역, 양재역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10km~50km

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
        And 지하철 20KM 구간 기본 요금 1250원 + 지하철 20KM 거리 추가 요금 200원 + 지하철 3호선 구간 추가 요금 900원 = 2350원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다 / 이동 거리 10 ~ 50km / 3호선 추가 요금 900원")
    @Test
    void findPathByDistance_distance_bet11ween_10km_50km() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 양재역, 고속터미널역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(양재역, 남부터미널역, 교대역, 고속터미널역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2350);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 50km이상

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함
        And 지하철 기본 요금 1250원 + 3호선 구간 추가 요금 900원 + 거리 추가 요금 10KM ~ 50KM 800원 + 거리 추가 요금 50KM ~ 66KM 200원 = 3150원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. 이동 거리 50km 이상 / 3호선 추가 요금 900원")
    @Test
    void findPathByDistance_distance_over_50km() {

        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 양재역, 신사역, DISTANCE);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(양재역, 남부터미널역, 교대역, 고속터미널역, 신사역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(66);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(3150);
    }

    /*
        Scenario: 두 역의 최소 시간 경로를 조회
        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 추가 금액 500이 부과되는 노선이 생성되어 있음

        When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
        Then 최소 시간 기준 경로를 응답
        And 총 거리(13KM)와 소요 시간을 함께 응답함
    */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {

        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 남부터미널역, 강남역, DURATION);

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 양재역, 강남역);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(15);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(13);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함 : 노선 기본 요금 1250원 + 3호선 추가 요금 900원 = 2150원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 이동 거리 10km 이하 / 추가 요금 노선 2개 가장 높은 금액 요금 적용 확인")
    @Test
    void findPathByDistance_distance_under_10km_multi_addFareLine() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(given(), 교대역, 양재역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함 : 노선 기본 요금 1250원git + 3호선 추가 요금 900원 = 2150원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 이동 거리 10km 이하 / 성인 / 3호선 추가 요금")
    @Test
    void findPathByDistance_distance_under_10km_multi_addFareLine1() {

        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(성인, 교대역, 양재역, DISTANCE);
        // then

        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함 노선 기본 요금 1250원 + 3호선 추가 요금 900원 = 2150원
        And 청소년 할인 (2150 - 350) * 0.8
        And 2150원 - 350원 공제 : 1800원  * 0.8 = 1440원
    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 이동 거리 10km 이하 / 청소년 / 3호선 추가 요금")
    @Test
    void findPathByDistance_distance_under_10km_multi_addFareLine2() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(청소년, 교대역, 양재역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1440);
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음
        And 조회하는 구간의 최단 거리가 10KM 이하

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
        And 총 거리와 소요 시간을 함께 응답함
        And 지하철 이용 요금도 함께 응답함 : 노선 기본 요금 1250원 + 3호선 추가 요금 900원 = 2150원
        And 어린이 할인 (2150 - 350) * 0.5 = 900원

    */
    @DisplayName("두 역의 최단 거리 경로를 조회 / 이동 거리 10km 이하 / 어린이 / 3호선 추가 요금")
    @Test
    void findPathByDistance_distance_under_10km_multi_addFareLine3() {

        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(어린이, 교대역, 양재역, DISTANCE);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(900);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int addFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("addFare", addFare + "");

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
