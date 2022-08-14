package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 양재시민의숲역;
    private Long 청계산입구역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재 --- *신분당선* --- 양재시민의숲 --- *신분당선* --- 청계산입구
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청(관리자, "양재시민의숲역").jsonPath().getLong("id");
        청계산입구역 = 지하철역_생성_요청(관리자, "청계산입구역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 2);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 3);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재역, 양재시민의숲역, 7, 3));
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, createSectionCreateParams(양재시민의숲역, 청계산입구역, 50, 15));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE");

        // then
        경로_검증(response, List.of(교대역, 남부터미널역, 양재역), 5, 6);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var response = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION");

        // then
        경로_검증(response, List.of(교대역, 강남역, 양재역), 20, 4);
    }

    @DisplayName("연결되어 있지 않은 역에 대하여 경로 조회 실패")
    @ParameterizedTest
    @EnumSource(value = PathType.class)
    void findPathFailsForStationsNotConnected(PathType type) {
        // given
        var 수유역 = 지하철역_생성_요청(관리자, "수유역").jsonPath().getLong("id");
        var 쌍문역 = 지하철역_생성_요청(관리자, "쌍문역").jsonPath().getLong("id");
        var 사호선 = 지하철_노선_생성_요청("4호선", "skyblue", 수유역, 쌍문역, 10, 2);

        // when
        var response = 두_역의_경로_조회를_요청(교대역, 수유역, type.name());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("동일한 역에 대하여 경로조회 실패")
    @ParameterizedTest
    @EnumSource(value = PathType.class)
    void findPathFailsForEqualStations(PathType type) {
        // when
        var response = 두_역의_경로_조회를_요청(교대역, 교대역, type.name());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("두 역의 경로 조회시 요금 반환 (10km 이하)")
    @Test
    void findFaresForPathWithDistanceLess10km() {
        // when
        var response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE");

        // then
        // 기본요금 1,250원
        거리와_요금_확인(response, 5, 1250);
    }

    @DisplayName("두 역의 경로 조회시 요금 반환 (10km 초과 50km 이하)")
    @Test
    void findFaresForPathWithDistanceLess50km() {
        // when
        var response = 두_역의_경로_조회를_요청(강남역, 양재시민의숲역, "DISTANCE");

        // then
        // 기본요금 + 10km 초과시 5km 마다 100원
        거리와_요금_확인(response, 17, 1250 + 200);
    }

    @DisplayName("두 역의 경로 조회시 요금 반환 (50km 초과)")
    @Test
    void findFaresForPathWithDistanceExcess50km() {
        // when
        var response = 두_역의_경로_조회를_요청(교대역, 청계산입구역, "DISTANCE");

        // then
        // 기본요금 + 10km 초과시 5km 마다 100원 + 50km 초과시 8km 마다 100원
        거리와_요금_확인(response, 62, 1250 + 800 + 200);
    }

    @DisplayName("운임을 실제 경로와 관계없이 최단경로 기준으로 책정")
    @Test
    void fareCalculatedByMinimumDistance() {
        // when
        var fareA = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE").jsonPath().getInt("fare");
        var fareB = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION").jsonPath().getInt("fare");

        // then
        assertEquals(fareA, fareB);
    }

    private void 경로_검증(ExtractableResponse<Response> response, List<Long> stations, int distance, int duration) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactlyElementsOf(stations);
    }

    private void 거리와_요금_확인(ExtractableResponse<Response> response, int distance, int fare) {
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    private ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
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
