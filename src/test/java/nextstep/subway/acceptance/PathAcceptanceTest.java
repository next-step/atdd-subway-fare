package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.노선_생성_요청값_생성;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 오금역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  -- *3호선* -- 오금
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        오금역 = 지하철역_생성_요청("오금역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 5, 200);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5, 1000);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 10, 5, 300);

        지하철_노선에_지하철_구간_생성_요청(삼호선, LineSteps.구간_생성_요청값_생성(남부터미널역, 양재역, 9, 6));
        지하철_노선에_지하철_구간_생성_요청(삼호선, LineSteps.구간_생성_요청값_생성(양재역, 오금역, 40, 20));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        경로_조회됨(response, List.of(교대역, 남부터미널역, 양재역), 19, 11, 1750);
    }

    @DisplayName("두 역의 최단 거리가 50km 초과 일 때 8km 마다 100원이 추가된다.")
    @Test
    void findPathOver50Km() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 오금역);

        // then
        경로_조회됨(response, List.of(교대역, 남부터미널역, 양재역, 오금역), 59, 31, 2550);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        //when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        //then
        경로_조회됨(response, List.of(교대역, 강남역, 양재역), 20, 10, 2450);
    }

    /**
     * Given: 어린이 회원을 생성한다.
     * Given: 어린이 회원이 로그인한다.
     * When: 두 역의 최단 거리 경로를 조회한다.
     * Then: 운임에서 350원을 공제한 금액의 50%할인된 금액이 반환된다.
     */
    @Test
    void findPathByChildrenMember() {
        //given
        MemberSteps.회원_생성_요청("children@gmail.com", "password", 12);
        String accessToken = 베어러_인증_로그인_요청("children@gmail.com", "password").jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(accessToken, 교대역, 양재역);

        // then
        경로_조회됨(response, List.of(교대역, 남부터미널역, 양재역), 19, 11, 700);
    }

    /**
     * Given: 청소년 회원을 생성한다.
     * Given: 청소년 회원이 로그인한다.
     * When: 두 역의 최단 거리 경로를 조회한다.
     * Then: 운임에서 350원을 공제한 금액의 20%할인된 금액이 반환된다.
     */
    @Test
    void findPathByTeenAgerMember() {
        //given
        MemberSteps.회원_생성_요청("teenager@gmail.com", "password", 18);
        String accessToken = 베어러_인증_로그인_요청("teenager@gmail.com", "password").jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(accessToken, 교대역, 양재역);

        // then
        경로_조회됨(response, List.of(교대역, 남부터미널역, 양재역), 19, 11, 1120);
    }

    /**
     * Given: 성인 회원을 생성한다.
     * Given: 성인 회원이 로그인한다.
     * When: 두 역의 최단 거리 경로를 조회한다.
     * Then: 할인되지 않은 운임이 반환된다.
     */
    @Test
    void findPathByAdultMember() {
        //given
        MemberSteps.회원_생성_요청("adult@gmail.com", "password", 19);
        String accessToken = 베어러_인증_로그인_요청("adult@gmail.com", "password").jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(accessToken, 교대역, 양재역);

        // then
        경로_조회됨(response, List.of(교대역, 남부터미널역, 양재역), 19, 11, 1750);
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surcharge) {
        Map<String, String> lineCreateParams = 노선_생성_요청값_생성(name, color, upStation, downStation, distance, duration, surcharge);

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

}
