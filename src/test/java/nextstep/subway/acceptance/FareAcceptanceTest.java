package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 요금 계산")
class FareAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10, 2분)* ---  강남역
     * |                             |
     * *3호선(2, 3분)*                 *신분당선(10, 1분)*
     * |                             |
     * 남부터미널역  --- *3호선(3, 3분)* ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 200);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 1, 1000);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 3, 300);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, final int duration, final int additionalFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionalFare", additionalFare + "");

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

    @DisplayName("두 역의 최단 거리 경로에 대한 요금을 조회한다. 비로그인 사용자는 할인 요금이 없다.")
    @Test
    void getPriceOfPathByDistanceNotLogin() {
        // given
        Long 잠실역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 이호선, createSectionCreateParams(강남역, 잠실역, 50, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청_로그인안됨(남부터미널역, 잠실역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(62L);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2550);
    }

    @DisplayName("두 역의 최단 거리 경로에 대한 요금을 조회한다. 할인 대상이 아닌 사용자는 할인 요금이 없다.")
    @Test
    void getPriceOfPathByDistanceNotTarget() {
        // given
        Long 잠실역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 이호선, createSectionCreateParams(강남역, 잠실역, 50, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(관리자, 남부터미널역, 잠실역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(62L);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2550);
    }

    @DisplayName("두 역의 최단 거리 경로에 대한 요금을 조회한다. 어린이는 50% 할인요금이 있다.")
    @Test
    void getPriceOfPathByDistanceByChild() {
        // given
        final String 어린이 = 로그인_되어_있음("child@email.com", "password");

        Long 잠실역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 이호선, createSectionCreateParams(강남역, 잠실역, 50, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(어린이, 남부터미널역, 잠실역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1100);
    }

    @DisplayName("두 역의 최단 거리 경로에 대한 요금을 조회한다. 청소년은 20% 할인요금이 있다.")
    @Test
    void getPriceOfPathByDistanceByTeenager() {
        // given
        final String 청소년 = 로그인_되어_있음("teenager@email.com", "password");
        Long 잠실역 = 지하철역_생성_요청(관리자, "고속터미널역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(관리자, 이호선, createSectionCreateParams(강남역, 잠실역, 50, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(청소년, 남부터미널역, 잠실역, "DISTANCE");

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1760);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청_로그인안됨(Long source, Long target, final String type) {
        return given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String token, Long source, Long target, final String type) {
        return given(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }
}
