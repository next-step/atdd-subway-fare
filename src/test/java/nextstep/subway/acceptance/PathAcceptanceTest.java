package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
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

    private String 어린이;
    private String 청소년;

    /**
     * 교대역    --- *2호선* (거리:10, 시간:2) ---   강남역
     * |                                        |
     * *3호선* (거리:2, 기간:31)                  *신분당선* (거리:10, 시간:3)
     * |                                        |
     * 남부터미널역  --- *3호선*(거리:3, 시간:21) ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 400);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3, 900);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 31, 0);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 21));

        어린이 = 베어러_인증_로그인_요청(GithubResponses.어린이.getEmail(), PASSWORD).jsonPath().getString("accessToken");
        청소년 = 베어러_인증_로그인_요청(GithubResponses.청소년.getEmail(), PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When: 교대-양재 최단거리 경로와 거리, 시간을 조회하면
     * Then: 경로는 교대역-남부터미널역-양재역이고, 거리는 5, 시간은 52분이다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다. (신분당선의 추가 요금도 함께 계산한다.)")
    @Test
    void findPathByDistance_DISTANCE() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, "DISTANCE");

        // then
        verifyPathResponse(response, 5L, 52L, 1250L, 교대역, 남부터미널역, 양재역);
    }

    /**
     * When: 교대-양재 최소소요시간 경로와 거리, 시간을 조회하면
     * Then: 경로는 교대역-강남역-양재역이고, 거리는 20, 시간은 5분이다.
     */
    @DisplayName("두 역의 최소 소요시간 경로를 조회한다.")
    @Test
    void findPathByDistance_DURATION() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, "DURATION");

        // then
        verifyPathResponse(response, 20L, 5L, 1250L, 교대역, 강남역, 양재역);
    }

    /**
     * When: 어린이 사용자가 교대-양재 최단거리와 요금을 조회하면
     * Then: 거리는 5이고, 요금은 800원이다.
     */
    @DisplayName("어린이 사용자가 두 역의 최소 최단경로 요금을 조회한다.")
    @Test
    void findPathByDistance_child_fare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 양재역, "DISTANCE");

        // then
        verifyPathResponse(response, 5L, 52L, 800L, 교대역, 남부터미널역, 양재역);
    }

    /**
     * When: 청소년 사용자가 교대-양재 최단거리와 요금을 조회하면
     * Then: 거리는 5이고, 요금은 1070원이다.
     */
    @DisplayName("청소년 사용자가 두 역의 최소 최단경로 요금을 조회한다.")
    @Test
    void findPathByDistance_teen_fare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 양재역, "DISTANCE");

        // then
        verifyPathResponse(response, 5L, 52L, 1070L, 교대역, 남부터미널역, 양재역);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int additionalFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionalFare", additionalFare + "");

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

    private void verifyPathResponse(ExtractableResponse<Response> response, Long distance, Long duration, Long fare, Long... stations) {
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getLong("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(fare);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
    }
}
