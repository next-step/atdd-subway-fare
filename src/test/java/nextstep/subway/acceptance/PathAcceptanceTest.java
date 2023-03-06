package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.SearchType;
import nextstep.subway.utils.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
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
    private String 성인;
    private String 청소년;
    private String 어린이;

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     * <이동거리|이동시간>
     * 교대역    --- *2호선* <10|10> ---   강남역
     * |                                    |
     * *3호선* <2|2>                       *신분당선* <10|10>
     * |                                    |
     * 남부터미널역  --- *3호선* <3|3> ---   양재역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 0, 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 500, 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 900,교대역, 남부터미널역, 2, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));

        성인 = 베어러_인증_로그인_요청(Users.성인.getEmail(), Users.성인.getPassword()).jsonPath().getString("accessToken");
        청소년 = 베어러_인증_로그인_요청(Users.청소년.getEmail(), Users.청소년.getPassword()).jsonPath().getString("accessToken");
        어린이 = 베어러_인증_로그인_요청(Users.어린이.getEmail(), Users.어린이.getPassword()).jsonPath().getString("accessToken");
    }
    
    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최단 거리 경로를 조회
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역, SearchType.DISTANCE.name());

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250 + 900)
        );
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 추가운임이 다른 노선을 경유하는 두 역의 최단 거리 경로를 조회
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("추가운임이 다른 노선을 경유하는 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance2() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(남부터미널역, 강남역, SearchType.DISTANCE.name());

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250 + 100 + 900)
        );
    }

    /**
     * Feature: 연령별 요금 정책
     *
     *   Scenario: 로그인 사용자는 연령별 요금으로 계산
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("어린이와 청소년이 아닌경우 운임의 변화는 없다.")
    @Test
    void fareByAge() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(성인, 남부터미널역, 강남역, SearchType.DISTANCE.name());

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250 + 100 + 900)
        );
    }

    /**
     * Feature: 연령별 요금 정책
     *
     *   Scenario: 로그인 사용자는 연령별 요금으로 계산
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("청소년은 운임에서 20%를 할인한다.")
    @Test
    void fareWhenChildren() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(청소년, 남부터미널역, 강남역, SearchType.DISTANCE.name());

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo((1_250 + 100 + 900) * 0.8)
        );
    }

    /**
     * Feature: 연령별 요금 정책
     *
     *   Scenario: 로그인 사용자는 연령별 요금으로 계산
     *     When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     *     Then 최단 거리 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("어린이는 운임에서 50%를 할인한다.")
    @Test
    void fareWhenTeenager() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(어린이, 남부터미널역, 강남역, SearchType.DISTANCE.name());

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(남부터미널역, 교대역, 강남역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(12),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo((1_250 + 100 + 900) * 0.5)
        );
    }

    /**
     * Feature: 지하철 경로 검색
     *
     *   Scenario: 두 역의 최소 시간 경로를 조회
     *     When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *     Then 최소 시간 기준 경로를 응답
     *     And 총 거리와 소요 시간을 함께 응답함
     *     And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역, SearchType.DURATION.name());

        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250 + 900)
        );
    }

    private Long 지하철_노선_생성_요청(String name, String color, int surcharge, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("surcharge", surcharge + "");

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
