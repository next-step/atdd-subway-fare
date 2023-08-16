package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청_후_id_추출;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_타입에따른_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

    private static final int DEFAULT_FARE = 1250;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |
     * | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청_후_id_추출("2호선", "green", 교대역, 강남역, 10, 30);
        신분당선 = 지하철_노선_생성_요청_후_id_추출("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청_후_id_추출("3호선", "orange", 교대역, 남부터미널역, 2, 20);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 30));
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId,
        int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 양재역, PathType.DISTANCE.name());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역,
            남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(50);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(DEFAULT_FARE);

    }

    /**
     * Feature: 지하철 경로 검색 Scenario: 두 역의 최소 시간 경로를 조회 Given 지하철역이 등록되어있음 And 지하철 노선이 등록되어있음 And 지하철
     * 노선에 지하철역이 등록되어있음 When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청 Then 최소 시간 기준 경로를 응답 And 총 거리와 소요 시간을
     * 함께 응답함 And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로 조회")
    @Test
    void findPathByDuration() {

        // when
        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 양재역, PathType.DURATION.name());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역,
            양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(20);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(40);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(DEFAULT_FARE + 200);
    }


    /**
     * 교대역    --- *2호선*(10km) ---   강남역    ----4호선 (2km, 800원) --- 짱짱역 |                        |
     * *3호선*                   *신분당선* |                        | 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("추가 요금이 있는 노선을 이용할 때 요금 조회")
    @Test
    void findPathForUsageFeeLine() {

        //given
        Long 짱짱역 = 지하철역_생성_요청("짱짱역").jsonPath().getLong("id");
        지하철_노선_생성_요청("4호선", "blue", 강남역, 짱짱역, 2, 20, 800);

        //when
        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 짱짱역, PathType.DISTANCE.name());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역,
            짱짱역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(12);

        int distanceOverFare = 100;
        int lineUsageOverFare = 800;

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(
            DEFAULT_FARE + distanceOverFare + lineUsageOverFare);
    }


    /**
     * 교대역    --- *2호선*(10km) ---   강남역  --4호선 (2km, 800원) --- 짱짱역 -- 5호선(1km, 300원)-- 굿역 |
     *               | *3호선*                   *신분당선* |                        | 남부터미널역  --- *3호선*
     * ---   양재
     */

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
    @Test
    void findTransferFee() {

        //given
        Long 짱짱역 = 지하철역_생성_요청("짱짱역").jsonPath().getLong("id");
        지하철_노선_생성_요청("4호선", "blue", 강남역, 짱짱역, 2, 20, 800);

        Long 굿역 = 지하철역_생성_요청("굿역").jsonPath().getLong("id");
        지하철_노선_생성_요청("5호선", "purple", 짱짱역, 굿역, 1, 10, 300);

        //when
        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 굿역, PathType.DISTANCE.name());

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역,
            짱짱역, 굿역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(13);

        int distanceOverFare = 100;
        int lineUsageOverFare = 800;

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(
            DEFAULT_FARE + distanceOverFare + lineUsageOverFare);
    }

    @DisplayName("로그인한 멤버의 유형별 할인 금액 조회 [어른, 어린이, 청소년] ")
    @ParameterizedTest
    @CsvSource(value = {"6,800", "13,1070", "27,1250"}, delimiter = ',')
    void findPathForMemberDiscount(int age, int fare) {

        String email = "email";
        String password = "password";

        var 회원 = 회원_생성_요청(email, password, age);
        var accessToken = 베어러_인증_로그인_요청(email, password).jsonPath().getString("accessToken");

        // when
        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 양재역, PathType.DISTANCE.name(), accessToken);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역,
            남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(50);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }

    @DisplayName("로그인하지 않아도 경로 조회 허용")
    @Test
    void pathIsAuthNonRequired() {

        var response = 두_역의_타입에따른_경로_조회를_요청(교대역, 양재역, PathType.DISTANCE.name());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
