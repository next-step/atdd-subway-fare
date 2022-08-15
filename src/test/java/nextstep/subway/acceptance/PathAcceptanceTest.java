package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 당산역;
    private Long 수원역;
    private Long 역삼역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private String 아이;
    private String 청소년;
    private String 어른;

    /**
     * 당산역
     * |
     * *2호선(50, 100)*
     * |
     * 교대역    --- *2호선(10   , 1)* ---   강남역    --- 2호선 (5, 2) --- 역삼역
     * |                                |
     * *3호선(2, 10)*                     *신분당선* (10, 1)
     * |                                |
     * 남부터미널역  --- *3호선(3,   5)* ---  양재
     *
     * 추가 요금
     * 2호선 - 500
     * 신분당선 - 1000
     * 삼호선 - 100
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        수원역 = 지하철역_생성_요청(관리자, "수원역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청(관리자, "역삼역").jsonPath().getLong("id");
        당산역 = 지하철역_생성_요청(관리자, "당산역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 1, 500);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 강남역, 역삼역, 5, 2, 500);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 당산역, 50, 100, 500);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 1, 1000);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 100);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));

        아이 = 로그인_되어_있음("child@email.com", "password");
        청소년 = 로그인_되어_있음("teenager@email.com", "password");
        어른 = 로그인_되어_있음("nineteen@email.com", "password");
    }

    /**
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 10km 이하)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     */
    @DisplayName("환승하지 않고 10km이하 최단 거리를 조회한 경우")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_거리_경로_조회를_요청(강남역 , 역삼역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 역삼역 }, 1750);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void transforFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_거리_경로_조회를_요청(강남역, 남부터미널역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 교대역, 남부터미널역 }, 1450);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 50km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("50km 거리 이상 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void overDistance50FindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_거리_경로_조회를_요청(남부터미널역, 당산역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 남부터미널역, 교대역, 당산역 }, 2250);
    }


    /**
     * 연결 되지 않은 지하철일 경우 반환합니다.
     *
    *  Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 연결되어 있지 않는 지하철역 최단거리를 조회
     * Then 400(Bad-Request) 응답
     * 
     */
    @DisplayName("연결 되지 않은 지하철역을 최단 거리를 조회할 경우 에러를 반환합니다.")
    @Test
    void notConnectionFindPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(강남역, 수원역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 시간 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     * (2호선) -> 교대역 -> 강남역 -> (신분당선 환승) -> 양재역
     */
    @DisplayName("환승하여 최단 시간걸리는 지하철 경로를 조회합니다")
    @Test
    void TransferFindPathByDuration() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);
        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 교대역, 강남역, 양재역 }, 1950);
    }

    /**
     * 환승하지 않고 최단 시간 걸리는 지하철 경로를 조회
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 시간 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     */
    @DisplayName("환승하지 않고 최단 시간걸리는 지하철 경로를 조회합니다")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_시간_경로_조회를_요청(역삼역, 교대역);
        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 역삼역, 강남역, 교대역 }, 1850);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 시간 경로 조회를 요청
     * Then 최단 거리 경로를 응답(총 거리 50km 이상)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @Test
    @DisplayName("50km 거리 이상 지하철 최단 시간 경로 테스트 합니다.")
    void overDistance50() {
        // when
        ExtractableResponse<Response> 경로_조회 = 두_역의_최단_시간_경로_조회를_요청(남부터미널역, 당산역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 남부터미널역, 양재역, 강남역, 교대역, 당산역 }, 2450);
    }

    /**
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 아이 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 이하)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     */
    @DisplayName("아이 할인 환승하지 않는 최단 거리 조회")
    @Test
    void childOfFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(아이, 강남역 , 역삼역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 역삼역 }, 700);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 아이 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("아이 할인 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void childOfTransforFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(아이, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 교대역, 남부터미널역 }, 550);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 아이 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 50km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("아이 할인 50km 거리 이상 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void childOfOverDistance50FindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(아이, 남부터미널역, 당산역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 남부터미널역, 교대역, 당산역 }, 950);
    }

    /**
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 청소년 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 이하)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     */
    @DisplayName("청소년 할인 환승하지 않는 최단 거리 조회")
    @Test
    void teenagerOfFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(청소년, 강남역 , 역삼역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 역삼역 }, 1120);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 청소년 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("청소년 할인 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void teenagerOfTransforFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(청소년, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 교대역, 남부터미널역 }, 880);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 청소녀 할인
     *
     * Then 최단 거리 경로를 응답(총 거리 50km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("청소년 할인 50km 거리 이상 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void teenagerOfㅒverDistance50FindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(청소년, 남부터미널역, 당산역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 남부터미널역, 교대역, 당산역 }, 1520);
    }

    /**
     *
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 어른은 그대로 반환
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 이하)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     *
     */
    @DisplayName("어른은 할인없음 환승하지 않는 최단 거리 조회")
    @Test
    void adultOfFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(어른, 강남역 , 역삼역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 역삼역 }, 1750);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 어른은 그대로 반환
     *
     * Then 최단 거리 경로를 응답(총 거리 10km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("어른은 할인없음 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void adultOfTransforFindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(어른, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 강남역, 교대역, 남부터미널역 }, 1450);
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * And 어른은 그대로 반환
     *
     * Then 최단 거리 경로를 응답(총 거리 50km 초과)
     * And 총 거리와 소요 시간을 함께 응답함
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("어른은 할인없음 50km 거리 이상 환승하여 두 역 최단 거리를 조회한 경우")
    @Test
    void adultOfㅒverDistance50FindPathByDistance() {
        // when
        ExtractableResponse<Response> 경로_조회 = 로그인_후_두_역의_최단_거리_경로_조회를_요청(어른, 남부터미널역, 당산역);

        // then
        최단_경로_및_요금_계산(경로_조회, new Long[] { 남부터미널역, 교대역, 당산역 }, 2250);
    }


    private void 최단_경로_및_요금_계산(ExtractableResponse<Response> response, Long[] stations, int price) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(stations);
        assertThat(response.jsonPath().getInt("price")).isEqualTo(price);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DISTANCE);
    }

    private ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return 두_역의_최단_경로_요청(source, target, PathType.DURATION);
    }

    private ExtractableResponse<Response> 로그인_후_두_역의_최단_거리_경로_조회를_요청(String token, Long source, Long target) {
        return given(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("pathType", PathType.DISTANCE.name())
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_경로_요청(Long source, Long target, PathType pathType) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("pathType", pathType.name())
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int additionPrice) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionPrice", additionPrice + "");

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
