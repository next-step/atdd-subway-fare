package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.ShortestPathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_후_로그인;
import static nextstep.subway.acceptance.PathSteps.로그인_상태로_타입별_최단_경로_조회_요청;
import static nextstep.subway.acceptance.PathSteps.타입별_최단_경로_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 판교역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * <p>
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역    --- *신분당선* ---    판교역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 3);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 900, 강남역, 양재역, 10, 4);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 4);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 판교역, 20, 21);
    }

    /**
     * Scenario: 두 역의 최소 거리 경로를 조회
     * When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청
     * Then 최소 거리 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 타입별_최단_경로_조회_요청(교대역, 양재역, ShortestPathType.DISTANCE);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(8),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5)
        );
    }

    /**
     * Scenario: 두 역의 최소 시간 경로를 조회
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByTime() {
        // when
        ExtractableResponse<Response> response = 타입별_최단_경로_조회_요청(교대역, 양재역, ShortestPathType.TIME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(7),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20)
        );
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 시간 경로의 요금을 조회한다.")
    @Test
    void findPathByTime_WithSubwayFare() {
        // when
        ExtractableResponse<Response> response = 타입별_최단_경로_조회_요청(교대역, 양재역, ShortestPathType.TIME);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(7),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_350)
        );
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 경로 조회하면
     * And  추가 요금이 있는 노선을 이용
     * Then 추가 요금이 계산된 이용요금을 응답함
     */
    @DisplayName("추가요금이 있는 노선의 경로를 조회한다.")
    @Test
    void findPathByDistance_WithAdditionalFareLine() {
        // when
        ExtractableResponse<Response> response = 타입별_최단_경로_조회_요청(교대역, 판교역, ShortestPathType.DISTANCE);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역, 판교역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(28),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(25),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_450)
        );
    }

    /**
     * Given 청소년 회원 생성 후 로그인 하고 (13세 <= 청소년 < 19세)
     * When 출발역에서 도착역까지의 최단 거리 경로 조회하면
     * Then 청소년 할인이 (이용요금 - 350) * 0.8 적용된 이용요금을 응답한다
     */
    @DisplayName("청소년 할인이 적용된 요금을 조회한다.")
    @Test
    void findPathByDistance_WithTeenagerDiscount() {
        // given
        String 회원토큰 = 회원_생성_후_로그인("13YearsOld@email.com", "passwd", 13).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_상태로_타입별_최단_경로_조회_요청(회원토큰, 교대역, 판교역, ShortestPathType.DISTANCE);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역, 판교역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(28),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(25),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_680)
        );
    }

    /**
     * Given 어린이 회원 생성 후 로그인 하고 (6세 <= 어린이 < 13세)
     * When 출발역에서 도착역까지의 최단 거리 경로 조회하면
     * Then 어린이 할인이 (이용요금 - 350) * 0.5 적용된 이용요금을 응답한다
     */
    @DisplayName("어린이 할인이 적용된 요금을 조회한다.")
    @Test
    void findPathByDistance_WithChildDiscount() {
        // given
        String 회원토큰 = 회원_생성_후_로그인("12YearsOld@email.com", "passwd", 12).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_상태로_타입별_최단_경로_조회_요청(회원토큰, 교대역, 판교역, ShortestPathType.DISTANCE);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역, 판교역),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(28),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(25),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_050)
        );
    }
}
