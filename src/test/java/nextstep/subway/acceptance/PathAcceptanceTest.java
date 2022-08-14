package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

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
     * 2호선 : + 900
     * 3호선 : + 400
     * 신분당선 : + 500
     *
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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2, 900);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 6, 1, 500);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2, 400);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {

        // given
        final String distance = "DISTANCE";

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, distance);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1650);
    }

    @DisplayName("두 역의 최소 시간의 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {

        // given
        String duration = "DURATION";

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, duration);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(16);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(3);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(2350);
    }

    @DisplayName("두 역의 최단 거리 경로(길이)에 따라 추가적인 요금을 계산한다. - 노선별 추가 요금 적용(가장 높은 금액만)")
    @Test
    void additionalFarePerLine() {

        // given
        final String type = "DISTANCE";

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, type);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1650);
    }


    @DisplayName("두 역의 최단 거리 경로(길이)에 따른 요금을 계산한다. - 청소년 할인 정책 적용")
    @Test
    void calculateFareApplyDiscountTeenager() {

        // given
        final String type = "DISTANCE";

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(관리자, 교대역, 양재역, type);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1390);
    }

    @DisplayName("두 역의 최단 거리 경로(길이)에 따른 요금을 계산한다. - 어린이 할인 정책 적용")
    @Test
    void calculateFareApplyDiscountFromChildren() {

        // given
        final String type = "DISTANCE";

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(일반멤버, 교대역, 양재역, type);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(5);
        assertThat(response.jsonPath().getLong("fare")).isEqualTo(1000);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String 관리자, Long source, Long target, String type) {
        return AcceptanceTestSteps.given(관리자)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int price) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("price", price + "");

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
