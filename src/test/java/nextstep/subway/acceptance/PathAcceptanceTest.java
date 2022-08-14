package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.path.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 도곡역;
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

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        도곡역 = 지하철역_생성_요청(관리자, "도곡역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 12, 4);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 50, 5);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(양재역, 도곡역, 52, 7));
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청
     * Then 총 거리와 소요 시간을 함께 응답함
     */
    @Test
    void 두_역_최단_거리_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, PathType.DISTANCE.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 총 거리와 소요 시간을 함께 응답함
     */
    @Test
    void 두_역_최소_시간_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, PathType.DURATION.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 10KM 이내 최단 거리 경로 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_10KM_이내_최단_거리_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, PathType.DISTANCE.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 10KM 초과 50KM 이하 최단 거리 경로 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_10KM_초과_50KM_이하_최단_거리_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(남부터미널역, 강남역, PathType.DISTANCE.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(14),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(6),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350)
        );
    }

    /**
     * When 출발역에서 도착역까지의 50KM 초과 경로 최단 거리 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_경로_50KM_초과_최단_거리_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(남부터미널역, 도곡역, PathType.DISTANCE.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 양재역, 도곡역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(55),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(10),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 10KM 이내 최소 시간 경로 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_10KM_이내_최소_시간_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, PathType.DURATION.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 10KM 초과 50KM 이하 최소 시간 경로 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_10KM_초과_50KM_이하_최소_시간_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(남부터미널역, 강남역, PathType.DURATION.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(14),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(6),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350)
        );
    }

    /**
     * When 출발역에서 도착역까지의 50KM 초과 최소 시간 경로 조회를 요청
     * Then 총 거리와 소요 시간, 요금을 함께 응답함
     */
    @Test
    void 두_역_경로_50KM_초과_최소_시간_경로_조회() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(남부터미널역, 도곡역, PathType.DURATION.name());

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 양재역, 도곡역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(55),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(10),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250)
        );
    }

    private ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String pathType) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&pathType={pathType}", source, target, pathType)
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
