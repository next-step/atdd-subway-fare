package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.domain.BaseMemberFarePolicy.EXEMPTION_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;

    /**
     *   Given 지하철역이 등록되어있음
     *   And 지하철 노선이 등록되어있음
     *   And 지하철 노선에 지하철역이 등록되어있음
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

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        Long 이호선 = 지하철_노선_생성_요청("2호선", "green", 300, 교대역, 강남역, 10, 3);
        Long 신분당선 = 지하철_노선_생성_요청("신분당선", "red", 500, 강남역, 양재역, 10, 2);
        Long 삼호선 = 지하철_노선_생성_요청("3호선", "orange", 900, 교대역, 남부터미널역, 2, 10);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    /**
     * Scenario: 두 역의 최소 거리 경로를 조회
     *   When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청
     *   Then 최소 거리 기준 경로를 응답
     *   And 총 거리와 소요 시간을 함께 응답함
     *   And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로를_응답한다(response, 교대역, 남부터미널역, 양재역);
        요금를_응답한다(response);
    }

    /**
     * Scenario: 두 역의 최소 시간 경로를 조회
     *   When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *   Then 최소 시간 기준 경로를 응답
     *   And 총 거리와 소요 시간을 함께 응답함
     *   And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로를_응답한다(response, 교대역, 강남역, 양재역);
        요금를_응답한다(response);
    }

    /**
     * Scenario: 어린이 회원이 두 역의 최소 시간 경로를 조회
     *   Given 6세 이상 ~ 13세 미만의 나이로 회원가입을 하고
     *   And 로그인을 하고
     *   When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *   Then 최소 시간 기준 경로를 응답
     *   And 총 거리와 소요 시간을 함께 응답함
     *   And 운임 요금 350원 공제된 금액의 50% 할인된 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("어린이 회원이 두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathOfChildrenMember() {
        // given
        String token = 간편_가입_및_로그인_요청("children@nextstep.camp", "atdd", 6);

        // when
        ExtractableResponse<Response> findPathResponse = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);
        ExtractableResponse<Response> memberFindPathResponse = 인증_회원_두_역의_최단_시간_경로_조회를_요청(token, 교대역, 양재역);

        // then
        경로를_응답한다(findPathResponse, 교대역, 강남역, 양재역);
        int nonMemberFare = findPathResponse.jsonPath().getInt("fare.amount");
        int memberFare = memberFindPathResponse.jsonPath().getInt("fare.amount");
        회원_요금을_검증한다(memberFare, nonMemberFare, 0.5);
    }

    /**
     * Scenario: 청소년 회원이 두 역의 최소 시간 경로를 조회
     *   Given 13세 이상 ~ 19세 미만의 나이로 회원가입을 하고
     *   And 로그인을 하고
     *   When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     *   Then 최소 시간 기준 경로를 응답
     *   And 총 거리와 소요 시간을 함께 응답함
     *   And 운임 요금 350원 공제된 금액의 20% 할인된 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("청소년 회원이 두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathOfTeenagerMember() {
        // given
        String token = 간편_가입_및_로그인_요청("teenager@nextstep.camp", "atdd", 13);

        // when
        ExtractableResponse<Response> findPathResponse = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);
        ExtractableResponse<Response> memberFindPathResponse = 인증_회원_두_역의_최단_시간_경로_조회를_요청(token, 교대역, 양재역);

        // then
        경로를_응답한다(findPathResponse, 교대역, 강남역, 양재역);
        int nonMemberFare = findPathResponse.jsonPath().getInt("fare.amount");
        int memberFare = memberFindPathResponse.jsonPath().getInt("fare.amount");
        회원_요금을_검증한다(memberFare, nonMemberFare, 0.8);
    }

    private String 간편_가입_및_로그인_요청(String email, String password, int age) {
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        return 베어러_인증_로그인_요청(email, password).jsonPath().getString("accessToken");
    }

    private void 경로를_응답한다(ExtractableResponse<Response> response, Long... ids) {
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(ids);
    }

    private void 요금를_응답한다(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getLong("fare.amount")).isGreaterThanOrEqualTo(0);
    }

    private void 회원_요금을_검증한다(int memberFare, int nonMemberFare, double discountRate) {
        assertThat(memberFare).isEqualTo((long) ((nonMemberFare - EXEMPTION_AMOUNT) * discountRate));
    }
}
