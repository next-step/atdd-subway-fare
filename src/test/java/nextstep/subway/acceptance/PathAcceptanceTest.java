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
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로를_조회요청;
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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3, 900);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5, 0);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var 최단_거리_경로 = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로가_순서에_따라_조회됨(최단_거리_경로, 교대역, 남부터미널역, 양재역);
        경로의_이용요금이_조회됨(최단_거리_경로, 1_250);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var 최소_시간_경로 = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로가_순서에_따라_조회됨(최소_시간_경로, 교대역, 강남역, 양재역);
        경로의_이용요금이_조회됨(최소_시간_경로, 2_350);
    }

    @DisplayName("어린이가 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance_어린이() {
        // when
        String 어린이 = 로그인_되어_있음("children@email.com", "password");
        var 최단_거리_경로 = 두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 양재역);

        // then
        경로가_순서에_따라_조회됨(최단_거리_경로, 교대역, 남부터미널역, 양재역);
        경로의_이용요금이_조회됨(최단_거리_경로, 1070);
    }

    @DisplayName("청소년이 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance_청소년() {
        // when
        String 청소년 = 로그인_되어_있음("teenager@email.com", "password");
        var 최단_거리_경로 = 두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 양재역);

        // then
        경로가_순서에_따라_조회됨(최단_거리_경로, 교대역, 남부터미널역, 양재역);
        경로의_이용요금이_조회됨(최단_거리_경로, 800);
    }

    private void 경로가_순서에_따라_조회됨(ExtractableResponse<Response> response, Long... stations) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
    }

    private void 경로의_이용요금이_조회됨(ExtractableResponse<Response> response, int fare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        final String type = "DISTANCE";
        return 두_역의_경로를_조회요청(given(), source, target, type);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String token, Long source, Long target) {
        final String type = "DISTANCE";
        return 두_역의_경로를_조회요청(given(token), source, target, type);
    }

    private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        final String type = "DURATION";
        return 두_역의_경로를_조회요청(given(), source, target, type);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("extraFare", extraFare + "");

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
