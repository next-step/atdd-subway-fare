package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

    private static final String PATHS_PATH = "/paths?source={source}&target={target}&type={type}";
    private static final int BASIC_FARE = 1250;
    private static final String DISTANCE = "distance";
    private static final String DURATION = "duration";

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

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 40, 8);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 50, 6);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 10, 4);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 30, 2));
    }

    /**
     * Given 지하철역, 노선, 구간이 등록됨
     * When 출발역에서 도착역까지 최단 거리 경로 조회 요청
     * Then 최단 거리 경로, 거리, 소요시간, 요금까지 함께 응답
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        int overTenKiloFare = BASIC_FARE + 600;
        int overFiftyKiloFare = BASIC_FARE + 800;

        // when
        ExtractableResponse<Response> 교대_남부터미널_경로 = 두_역의_최단_거리_경로_조회를_요청(교대역, 남부터미널역);
        ExtractableResponse<Response> 교대_양재_경로 = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);
        ExtractableResponse<Response> 강남_양재_경로 = 두_역의_최단_거리_경로_조회를_요청(강남역, 양재역);

        // then
        경로확인(교대_남부터미널_경로, 교대역, 남부터미널역);
        요금확인(교대_남부터미널_경로, BASIC_FARE);
        경로확인(교대_양재_경로, 교대역, 남부터미널역, 양재역);
        요금확인(교대_양재_경로, overTenKiloFare);
        경로확인(강남_양재_경로, 강남역, 양재역);
        요금확인(강남_양재_경로, overFiftyKiloFare);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 이용 요금도 함께 응답
     */
    @DisplayName("최단 시간 경로 조회")
    @Test
    void findMinDurationPath() {
        // given
        int overTenKiloFare = BASIC_FARE + 600;

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회_요청(교대역, 양재역);
        int totalDistance = response.jsonPath().getInt("distance");
        int totalDuration = response.jsonPath().getInt("duration");
        int fare = response.jsonPath().getInt("fare");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        경로확인(response, 교대역, 남부터미널역, 양재역);
        assertThat(totalDistance).isEqualTo(40);
        assertThat(totalDuration).isEqualTo(6);
        assertThat(fare).isEqualTo(overTenKiloFare);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PATHS_PATH, source, target, DISTANCE)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회_요청(final long source, final long target) {
        return RestAssured.given().log().all()
                .when().get(PATHS_PATH, source, target, DURATION)
                .then().log().all()
                .extract();
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

    private void 경로확인(ExtractableResponse<Response> response, Long... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsExactly(stationIds);
    }

    private void 요금확인(ExtractableResponse<Response> response, int fare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

}
