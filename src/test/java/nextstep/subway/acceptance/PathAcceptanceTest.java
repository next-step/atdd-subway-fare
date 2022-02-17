package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성됨;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    public static final String BABY_EMAIL = "email1@email.com";
    public static final String CHILD_EMAIL = "email2@email.com";
    public static final String YOUTH_EMAIL = "email3@email.com";
    public static final String ADULT_EMAIL = "email4@email.com";

    public static final String PASSWORD = "password";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private String 유아사용자;
    private String 어린이사용자;
    private String 청소년사용자;
    private String 어른사용자;

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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 60, 1);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 20, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 30, 4));

        회원_생성됨(회원_생성_요청(BABY_EMAIL, PASSWORD, 1));
        회원_생성됨(회원_생성_요청(CHILD_EMAIL, PASSWORD, 6));
        회원_생성됨(회원_생성_요청(YOUTH_EMAIL, PASSWORD, 13));
        회원_생성됨(회원_생성_요청(ADULT_EMAIL, PASSWORD, 20));

        유아사용자 = 로그인_되어_있음(BABY_EMAIL, PASSWORD);
        어린이사용자 = 로그인_되어_있음(CHILD_EMAIL, PASSWORD);
        청소년사용자 = 로그인_되어_있음(YOUTH_EMAIL, PASSWORD);
        어른사용자 = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);
    }

    @DisplayName("로그인하지 않은 사용자의 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findNonLoggedInUserPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로_확인(response, 50, 6, 교대역, 남부터미널역, 양재역);
        요금_확인(response, 2_150);
    }

    @DisplayName("어른 사용자의 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(어른사용자, 교대역, 양재역);

        // then
        경로_확인(response, 50, 6, 교대역, 남부터미널역, 양재역);
        요금_확인(response, 2_150);
    }

    @DisplayName("어른 사용자의 두 역의 최소 시간 거리 경로를 조회한다.")
    @Test
    void findPathByTime() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(어른사용자, 교대역, 양재역);

        // then
        경로_확인(response, 70, 4, 교대역, 강남역, 양재역);
        요금_확인(response, 2_150);
    }

    @Test
    @DisplayName("어린이의 두 역의 최소 시간 거리 경로를 조회한다")
    void findChildPathByTime() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(어린이사용자, 교대역, 양재역);

        // then
        경로_확인(response, 70, 4, 교대역, 강남역, 양재역);
        요금_확인(response, 1_250);
    }

    @Test
    @DisplayName("유아의 두 역의 최소 시간 거리 경로를 조회한다")
    void findBabyPathByTime() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(유아사용자, 교대역, 양재역);

        // then
        경로_확인(response, 70, 4, 교대역, 강남역, 양재역);
        요금_확인(response, 0);
    }

    @Test
    @DisplayName("청소년의 두 역의 최소 시간 거리 경로를 조회한다")
    void findYouthPathByTime() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(청소년사용자, 교대역, 양재역);

        // then
        경로_확인(response, 70, 4, 교대역, 강남역, 양재역);
        요금_확인(response, 1_790);
    }

    private void 경로_확인(final ExtractableResponse<Response> response, final int distance, final int duration, Long... stationIds) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stationIds);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
    }

    private void 요금_확인(final ExtractableResponse<Response> response, final int fare) {
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
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
}
