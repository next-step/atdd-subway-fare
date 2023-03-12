package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 논현역;
    private Long 신논현역;
    private Long 논현선;

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
        논현역 = 지하철역_생성_요청("논현역").jsonPath().getLong("id");
        신논현역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 15, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 58, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);
        논현선 = 지하철_노선_생성_요청("논현선", "orange", 논현역, 신논현역, 58, 5);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 2));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE", "DISTANCE");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    private ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(
                        identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최소 시간 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void test1() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION", "DURATION");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(7);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(5km)(10KM 이내)(기본운임 1,250원)")
    @Test
    void test2() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE", "FARE_5");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(15km)(10km초과 ∼ 50km까지)(5km마다 100원)")
    @Test
    void test3() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 강남역, "DISTANCE", "FARE_15");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(58km)(10km초과 ∼ 50km까지)(8km마다 100원)")
    @Test
    void test4() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(논현역, 신논현역, "DISTANCE", "FARE_58");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(논현역, 신논현역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1850);
    }

}
