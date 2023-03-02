package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.fixtures.PathFixtures.DEFAULT_FEE;
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
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * <p>
     * 10
     * 교대역  --- *2호선* ---   강남역
     * |                        |
     * 2  *3호선*                   *신분당선*    10
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * 3
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = extractId(지하철역_생성_요청("교대역"));
        강남역 = extractId(지하철역_생성_요청("강남역"));
        양재역 = extractId(지하철역_생성_요청("양재역"));
        남부터미널역 = extractId(지하철역_생성_요청("남부터미널역"));

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 4);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 6);
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathSearchType.DISTANCE);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하고
     * And 총 거리와 소요 시간을 함께 응답한다
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다")
    @Test
    void 최소시간기준_경로_조회_요청시_최소시간기준_경로를_응답한다() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역, PathSearchType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하고
     * And 총 거리와 소요 시간을 함께 응답한다
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("최단거리기준 거리가 10km 이내일경우 기본운임으로 계산된다")
    @Test
    void 최단거리기준_거리가_10km_이내일경우_기본운임으로_계산된다() {
        // given


        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 강남역, PathSearchType.DISTANCE);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(DEFAULT_FEE);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하고
     * And 총 거리와 소요 시간을 함께 응답한다
     * And 지하철 이용 요금도 함께 응답함
     * <p>
     * 10
     * 교대역  --- *2호선* ---   강남역
     * |                        |
     * 2  *3호선*                   *신분당선*    10
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재 --- 학여울
     * 3              10
     */
    @DisplayName("최단거리기준 거리가 15km 일경우 기본운임에 200원이 추가된다.")
    @Test
    void 최단거리기준_거리가_15km_일경우_기본운임에_200원이_추가된다() {
        // given
        Long 학여울역 = extractId(지하철역_생성_요청("학여울역"));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(양재역, 학여울역, 10, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 학여울역, PathSearchType.DISTANCE);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(DEFAULT_FEE + 200);
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하고
     * And 총 거리와 소요 시간을 함께 응답한다
     * And 지하철 이용 요금도 함께 응답함
     * <p>
     * 10
     * 교대역  --- *2호선* ---   강남역
     * |                        |
     * 2  *3호선*                   *신분당선*    10
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재 --- 학여울 --- 대청 --- 가락시장 --- 오금역
     * 3              10       10      20         10
     */
    @DisplayName("최단거리기준 거리가 55km 일경우 기본운임에 700원이 추가된다.")
    @Test
    void 최단거리기준_거리가_55km_일경우_기본운임에_700원이_추가된다() {
        // given
        Long 학여울역 = extractId(지하철역_생성_요청("학여울역"));
        Long 대청역 = extractId(지하철역_생성_요청("대청역"));
        Long 가락시장 = extractId(지하철역_생성_요청("가락시장"));
        Long 오금역 = extractId(지하철역_생성_요청("오금역"));


        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(양재역, 학여울역, 10, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(학여울역, 대청역, 10, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(대청역, 가락시장, 20, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(가락시장, 오금역, 10, 3));

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 오금역, PathSearchType.DISTANCE);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(DEFAULT_FEE + 700);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(
            Long source,
            Long target,
            PathSearchType searchType) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(
                        "/paths?source={sourceId}&target={targetId}&type={searchType}",
                        source,
                        target,
                        searchType
                )
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(
            Long source,
            Long target,
            PathSearchType searchType) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source={sourceId}&target={targetId}&type={searchType}",
                        source,
                        target,
                        searchType
                )
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

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
