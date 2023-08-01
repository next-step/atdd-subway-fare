package nextstep.subway.acceptance;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
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

    private final int 교대역_강남역_거리 = 10;
    private final int 교대역_강남역_시간 = 3;
    private final int 강남역_양재역_거리 = 10;
    private final int 강남역_양재역_시간 = 5;
    private final int 교대역_남부터미널역_거리 = 2;
    private final int 교대역_남부터미널역_시간 = 2;
    private final int 남부터미널역_양재역_거리 = 3;
    private final int 남부터미널역_양재역_시간 = 10;

    private RequestSpecification requestSpecification;

    /**
     * (d=distance, t=duration)
     * 교대역    --- *2호선* ---        강남역
     * |              (d10,t3)          |
     * *3호선* (d2,t2)        (d10, t5) *신분당선*
     * |                 (d3,t10)       |
     * 남부터미널역  --- *3호선* ---      양재
     */
    // given
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 교대역_강남역_거리, 교대역_강남역_시간);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 강남역_양재역_거리, 강남역_양재역_시간);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 교대역_남부터미널역_거리
                , 교대역_남부터미널역_시간);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 남부터미널역_양재역_거리
                , 남부터미널역_양재역_시간));

        requestSpecification = new RequestSpecBuilder().build();
    }

    /**
     * Given 지하철역과 지하철노선에 역을 등록하고
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청하면
     * Then 최단 거리 기준 경로를 응답한다
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathBy_Distance() {
        // when
        var response = 두_역의_경로_조회를_요청(requestSpecification, 교대역, 양재역, PathType.DISTANCE.toString());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    /**
     * Given 지하철역과 지하철노선에 역을 등록하고
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답한다
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathBy_Duration() {
        // when
        var response = 두_역의_경로_조회를_요청(requestSpecification, 교대역, 양재역, PathType.DURATION.toString());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
    }

    /**
     * Given 지하철역과 지하철노선에 역을 등록하고
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청하면
     * Then 최단 거리 기준 경로를 응답한다
     * And 총 거리, 소요 시간, 지하철 이용 요금을 함께 응답한다
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathBy_Distance_Fare() {
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance
            , int duration) {
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

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance
            , int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
