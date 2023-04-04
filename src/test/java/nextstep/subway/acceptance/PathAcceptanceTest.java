package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.path.PathSearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 가상의역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long Binary호남선;

    int 교대역_강남역_시간 = 9;
    int 강남_양재_시간 = 10;
    int 교대_남부터미널_시간 = 1;
    int 가상_남부터미널_시간 = 1;
    int 남부터미널_양재_시간 = 2;

    /**
     * -------------------------- 교대역    --- *2호선* ---   강남역
     * --------------------------  |                         |
     * -------------------------- *3호선*                   *신분당선*
     * --------------------------  |                        |
     * *가상의역* --- *호남선* --- 남부터미널역 --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        가상의역 = 지하철역_생성_요청("가상의역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 교대역_강남역_시간);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 강남_양재_시간);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 교대_남부터미널_시간);
        Binary호남선 = 지하철_노선_생성_요청("Binary는 호남선", "ruby", 가상의역, 남부터미널역, 2, 가상_남부터미널_시간);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 남부터미널_양재_시간));
    }

    /**
     * Circular 경로는 생성 불가.
     */
    @DisplayName("지하철 경로 검색: 두 역의 최소 시간 경로를 조회")
    @Test
    void searchPath() {
        // given

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(강남역, 가상의역, PathSearch.DURATION);

        // then
        Integer duration = response.jsonPath().<Integer>get("duration");
        int 경로_1_최소시간 = 가상_남부터미널_시간 + 교대_남부터미널_시간 + 교대역_강남역_시간;
        int 경로_2_최소시간 = 가상_남부터미널_시간 + 남부터미널_양재_시간 + 강남_양재_시간;

        int 최소시간 = Math.min(경로_1_최소시간, 경로_2_최소시간);
        assertThat(duration).isEqualTo(최소시간);

    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target,  PathSearch type) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&searchType={type}", source, target, type)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&searchType=DISTANCE", source, target)
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

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance,
        int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }
}
