package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 수원역;
    private Long 역삼역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10   , 1)* ---   강남역    --- 2호선 (5, 2 ) --- 역삼역
     * |                                |
     * *3호선(2, 10)*                     *신분당선* (10, 1)
     * |                                |
     * 남부터미널역  --- *3호선(3,   5)* ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        수원역 = 지하철역_생성_요청(관리자, "수원역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청(관리자, "역삼역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 1);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 강남역, 역삼역, 5, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 1);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
    }

    /**
     * 환승하지 않고 최단거리를 가는 테스트를 합니다.
     *
     * 교대역 -> 남부터미널역 -> 양재역 (3호선, 거리:5, 시간: 15)
     */
    @DisplayName("환승하지 않고 두 역 최단 거리를 조회한 경우")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    /**
     * 환승을 하여 최단 거리를 가는 경우를 테스트합니다.
     *
     * 강남역 -> 교대역 -> (3호선환승) -> 남부터미널역
     */
    @DisplayName("환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void transforFindPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(강남역, 남부터미널역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 교대역, 남부터미널역);
    }

    /**
     * 연결 되지 않은 지하철일 경우 반환합니다.
     *
     * BAD_REQUEST - 400
     * 
     */
    @DisplayName("연결 되지 않은 지하철역을 최단 거리를 조회할 경우 에러를 반환합니다.")
    @Test
    void notConnectionFindPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(강남역, 수원역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 환승하여 최단 시간걸리는 지하철 경로를 조회합니다.
     *
     * (2호선) -> 교대역 -> 강남역 -> (신분당선 환승) -> 양재역
     */
    @DisplayName("환승하여 최단 시간걸리는 지하철 경로를 조회합니다")
    @Test
    void TransferFindPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
    }

    /**
     * 환승하지 않고 최단 시간 걸리는 지하철 경로를 조회
     *
     * (2호선) -> 강남역 ->
     *
     */
    @DisplayName("환승하지 않고 최단 시간걸리는 지하철 경로를 조회합니다")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(역삼역, 교대역);
        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(역삼역, 강남역, 교대역);
    }

    /**
     * 환승하지 않고 최단 시간 걸리는 지하철 경로를 조회
     *
     * (2호선) -> 강남역 ->
     *
     */
    @DisplayName("연결되어 있지 않은 지하철역 조회시 최단 시간걸리는 지하철 경로를 조회합니다")
    @Test
    void notConnectionFindPathByDurationException() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(역삼역, 교대역);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DISTANCE);
    }

    private ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DURATION);
    }

    private ExtractableResponse<Response> 두_역의_최단_경로_요청(Long source, Long target, PathType pathType) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("pathType", pathType.name())
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
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
