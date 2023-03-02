package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 판교역;
    private Long 이매역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 신신분당선;
    private Long 경강선;
    private String 청소년_AccessToken;
    private String 어린이_AccessToken;


    /**
     *ㅤㅤㅤㅤㅤㅤㅤㅤ시간: 2 / 거리: 2 <br>
     * ㅤ교대역 ----- *2호선*(0원) ---- 강남역 <br>
     * ㅤㅤ|ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ| <br>
     * ㅤㅤ|ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ| <br>
     * 시간: 3 / 거리: 2ㅤㅤㅤㅤㅤㅤㅤ시간: 2 / 거리: 3    <br>
     * *3호선*(0원)ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ*신분당선*(0원)  <br>
     * ㅤㅤ|ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ|  <br>
     * ㅤㅤ|ㅤㅤㅤㅤㅤㅤ시간: 2 / 거리: 2 ㅤㅤ|  <br>
     * 남부터미널역ㅤ---ㅤ*3호선*(0원) -----ㅤ양재역<br>
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ|<br>
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ|<br>
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ시간: 2 / 거리: 2
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ*신신분당선*(1500원) <br>
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤㅤ|<br>
     * ㅤㅤㅤㅤㅤㅤㅤㅤㅤ시간: 2 / 거리: 2ㅤㅤ |<br>
     * ㅤㅤ이매역ㅤ --  경강선(1000원) ---ㅤ판교역ㅤ<br>
     *
     * <br>
     * 
     * GIVEN: 지하철역이 등록되어있음 <br>
     * GIVEN: 지하철 노선이 등록되어있음 <br>
     * GIVEN: 지하철 노선에 지하철역이 등록되어있음<br>
     * GIVEN 청소년, 어린이가 회원가입을 한다<br>
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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 2, 2, 0).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 3, 2, 0).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 3, 0).jsonPath().getLong("id");
        신신분당선 = 지하철_노선_생성_요청("신신분당선", "black", 양재역, 판교역, 2, 2, 1500).jsonPath().getLong("id");
        경강선 = 지하철_노선_생성_요청("경강선", "blue", 판교역, 이매역, 2, 2, 1000).jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 2, 2);

        회원_생성_요청("teen@gmail.com", "qwe123", 13);
        var 청소년_베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("teen@gmail.com", "qwe123");
        청소년_AccessToken = Access_Token을_가져온다(청소년_베어러_인증_로그인_응답);

        회원_생성_요청("baby@gmail.com", "qwe123", 6);
        var 어린이_베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("baby@gmail.com", "qwe123");
        어린이_AccessToken = Access_Token을_가져온다(어린이_베어러_인증_로그인_응답);
    }

    /**
     * WHEN 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청 <br>
     * THEN 최소 거리 기준 경로를 응답 <br>
     * THEN 총 거리와 소요 시간을 함께 응답함 <br>
     * THEN 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로와_총_거리_총_소요시간_이용요금이_조회됨(response, List.of(교대역, 남부터미널역, 양재역), 4, 5, 1_250);
    }

    /**
     * WHEN 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청 <br>
     * THEN 최소 시간 기준 경로를 응답 <br>
     * THEN 총 거리와 소요 시간을 함께 응답함 <br>
     * THEN 지하철 이용 요금도 함께 응답함 <br>
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

        // then
        경로와_총_거리_총_소요시간_이용요금이_조회됨(response, List.of(교대역, 강남역, 양재역), 5, 4,  1_250);
    }

    /**
     * WHEN 출발역에서 도착역까지 최소 시간 기준으로 경로 조회를 요청<br>
     * THEN 경로를 지나는 노선들의 추가 요금 중 가장 비싼 추가 요금이 추가된다
     */
    @DisplayName("추가 요금 부과")
    @Test
    void findPathAdditionalLineFare() {
        // given
        var 강남_이매_예상_요금 = 1_250 + 1_500; // 기본요금 + 노선 추가요금

        // when
        var 두_역의_최단_시간_경로_조회를_응답 = 두_역의_최단_시간_경로_조회를_요청(강남역, 이매역);

        // then
        경로와_총_거리_총_소요시간_이용요금이_조회됨(두_역의_최단_시간_경로_조회를_응답, List.of(강남역, 양재역, 판교역, 이매역), 7, 6,  강남_이매_예상_요금);
    }

    /**
     * WHEN 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청 <br>
     * THEN 13세이상, 19세미만인 회원은 청소년 할인을 받는다<br>
     */
    @DisplayName("두 역의 최단 시간 경로를 조회 - 청소년 할인")
    @Test
    void findPathByDiscountTeenagerDiscount() {
        // given
        var 예상_요금 = 1250 - ((1_250 - 350) * 20) / 100; // 기본요금 - 청소년 할인 요금

        // when
        var response = 두_역의_최단_시간_경로_조회를_요청(청소년_AccessToken, 교대역, 양재역);

        // then
        경로와_총_거리_총_소요시간_이용요금이_조회됨(response, List.of(교대역, 강남역, 양재역), 5, 4,  예상_요금);
    }

    /**
     * WHEN 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청 <br>
     * THEN 6세이상, 13세미만인 회원은 어린이 할인을 받는다<br>
     */
    @DisplayName("두 역의 최단 시간 경로를 조회 - 어린이 할인")
    @Test
    void findPathByDiscountChildrenDiscount() {
        // given
        var 예상_요금 = 1250 - ((1_250 - 350) * 50) / 100; // 기본요금 - 어린이 할인 요금

        // when
        var response = 두_역의_최단_시간_경로_조회를_요청(어린이_AccessToken, 교대역, 양재역);

        // then
        경로와_총_거리_총_소요시간_이용요금이_조회됨(response, List.of(교대역, 강남역, 양재역), 5, 4,  예상_요금);
    }
}
