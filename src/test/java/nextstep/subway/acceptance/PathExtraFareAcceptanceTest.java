package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("추가 요금 및 할인 존재하는 지하철 경로 검색")
public class PathExtraFareAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 판교역;
    private Long 이매역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 경강선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                          |
     *                          |    * 경강선 *
     *                          판교 ----- 이매
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        이매역 = 지하철역_생성_요청("이매역").jsonPath().getLong("id");

        이호선 = 추가요금_존재하는_지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5, 100);
        신분당선 = 추가요금_존재하는_지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4, 200);
        삼호선 = 추가요금_존재하는_지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 300);
        경강선 = 추가요금_존재하는_지하철_노선_생성_요청("경강선", "blue", 판교역, 이매역, 10, 10, 400);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 10);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 판교역, 51, 100);
    }

    /**
     * Given 노선에 추가 요금이 주어지고,
     * When 최단 경로를 구할 경우,
     * Then 계산되는 최종 요금에 각 노선의 추가 요금의 최대값이 더해진다.
     */
    @DisplayName("노선에 추가 요금이 있는 경우, 최단 경로에 포함된 노선 중 노선의 추가 요금이 가장 큰 값이 최종 운임에 추가로 부여된다.")
    @Test
    void calculateFareWithLineExtraFare() {
        // when
        var response = 두_역의_최단_거리_경로_조회를_요청(교대역, 이매역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2350);
    }

    /**
     * Given 어린이 사용자가 로그인하고
     * When 최단 경로를 구할 경우,
     * Then 계산되는 최종 요금에 350원을 공제하고 나머지 금액의 50%를 할인한다.
     */
    @DisplayName("어린이 사용자가 최단 경로를 구할 경우, 계산한 요금에 350원을 공제하고 나머지 금액의 50%을 할인한다.")
    @Test
    void calculateFareWithChildUser() {
        // given
        String token = MemberSteps.베어러_인증_로그인_요청("children@email.com", "password").jsonPath().getString("accessToken");

        // when
        var response = 로그인_사용자_두_역의_최단_거리_경로_조회를_요청(token, 교대역, 이매역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1000);
    }

    /**
     * Given 청소년 사용자가 로그인하고
     * When 최단 경로를 구할 경우,
     * Then 계산되는 최종 요금에 350원을 공제하고 나머지 금액의 20%를 할인한다.
     */
    @DisplayName("청소년 사용자가 최단 경로를 구할 경우, 계산한 요금에 350원을 공제하고 나머지 금액의 20%를 할인한다.")
    @Test
    void calculateFareWithTeenAgeUser() {
        // given
        String token = MemberSteps.베어러_인증_로그인_요청("teenager@email.com", "password").jsonPath().getString("accessToken");

        // when
        var response = 로그인_사용자_두_역의_최단_거리_경로_조회를_요청(token, 교대역, 이매역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1600);
    }

    /**
     * Given 성인 사용자가 로그인하고
     * When 최단 경로를 구할 경우,
     * Then 별도의 할인 없이 최종 요금이 계산된다.
     */
    @DisplayName("성인 사용자가 최단 경로를 구할 경우, 별도의 할인 없이 최종 요금을 계산한다.")
    @Test
    void calculateFareWithAdultUser() {
        // given
        String token = MemberSteps.베어러_인증_로그인_요청("adult@email.com", "password").jsonPath().getString("accessToken");

        // when
        var response = 로그인_사용자_두_역의_최단_거리_경로_조회를_요청(token, 교대역, 이매역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2350);
    }
}
