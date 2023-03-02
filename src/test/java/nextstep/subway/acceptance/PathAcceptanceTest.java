package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathRequestType;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.로그인_후_두_역의_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3, 1000);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 0);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_거리_경로_조회를_요청(교대역, 양재역, PathRequestType.DISTANCE);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );

    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_거리_경로_조회를_요청(교대역, 양재역, PathRequestType.DURATION);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2450)
        );
    }

    @DisplayName("청소년으로 로그인 후 두 역의 경로를 조회한다.")
    @Test
    void findPathWhenTeen() {
        // when
        String accessToken = 깃허브_인증_로그인_요청(GithubResponses.청소년.getCode()).jsonPath().getString("accessToken");
        ExtractableResponse<Response> response = 로그인_후_두_역의_거리_경로_조회를_요청(accessToken, 교대역, 양재역, PathRequestType.DURATION);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1680)
        );
    }

    @DisplayName("어린이로 로그인 후 두 역의 경로를 조회한다.")
    @Test
    void findPathWhenChildren() {
        // when
        String accessToken = 깃허브_인증_로그인_요청(GithubResponses.어린이.getCode()).jsonPath().getString("accessToken");
        ExtractableResponse<Response> response = 로그인_후_두_역의_거리_경로_조회를_요청(accessToken, 교대역, 양재역, PathRequestType.DURATION);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1050)
        );
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surcharge) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("surcharge", surcharge + "");

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
}
