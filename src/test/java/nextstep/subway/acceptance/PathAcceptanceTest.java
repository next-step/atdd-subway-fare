package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.AuthAcceptanceTest.AGE_ADULT;
import static nextstep.subway.acceptance.AuthAcceptanceTest.AGE_CHILD;
import static nextstep.subway.acceptance.AuthAcceptanceTest.AGE_YOUTH;
import static nextstep.subway.acceptance.AuthAcceptanceTest.EMAIL;
import static nextstep.subway.acceptance.AuthAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathSteps.경로조회의_결과_경로가_예상과_같다;
import static nextstep.subway.acceptance.PathSteps.경로조회의_결과_정보가_예상과_같다;
import static nextstep.subway.acceptance.PathSteps.유저가_두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.유저가_두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 삼성역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 대치역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private String invalidToken = "invalidToken";
    private final String time = "202202200600";
    private final int 이호선노선요금 = 900;
    private final int 신분당선노선요금 = 2000;
    private final int 삼호선노선요금 = 1100;

    /**
     * 교대역    --- *2호선*  ---    강남역  ---  *2호선*  --- 삼성역
     *  |      거리:8, 시간:3        |       거리:8, 시간 3
     *  |                          |
     * *3호선*                   *신분당선*
      거리:6, 시간:5             거리:9, 시간:2
        |                          |
     *  |                          |
     * 남부터미널역  --- *3호선* ---  양재역 ---  *3호선*  ---  대치역
     *           거리:6, 시간:5             거리: 2, 시간 5
     *
     * 2호선: 추가요금 900원
     *      첫차시간 05:00
     *      막차시간 23:30
     *      운행간격 10분
     * 3호선: 추가요금 1100원
     *      첫차시간 05:30
     *      막차시간 23:00
     *      운행간격 5분
     * 신분당선: 추가요금 2000원
     *      첫차시간 05:20
     *      막차시간 23:40
     *      운행간격 20분
     * 청소년(13세 이상~19세 미만): 운임에서 350원 공제 후 20% 할인
     * 어린이(6세 이상~ 13세 미만): 운임에서 350원 공제 후 50% 할인
     *
     * *********************************************************************
     * **강남역에서~ 남부터미널역으로 06:00(202202200600) 출발해서 갈 경우**
     * 최단시간(운행시간고려):
     * 1. 강남역-양재역-남부터미널역  **(운행시간고려 최단시간 도착)**
     *      강남역에서 06:00 탑승
     *      양재역에서 06:02 하차
     *      양재역에서 06:05 탑승
     *      남부터미널역에서 06:10 하차 (도착)
     * 2. 강남역-교대역-남부터미널역
     *      강남역에서 06:03 탑승
     *      교대역에서 06:06 하차
     *      교대역에서 06:10 탑승
     *      남부터미널역에서 06:15 하차 (도착)
     * 요금(1. 기준): 1250(기본요금) + 100(추가요금) + 2000(신분당선, 경유노선최대요금)
     *      일반: 3350원
     *      청소년: 2400원
     *      어린이: 1500원
     *
     * 최단거리: 강남역-교대역-남부터미널역 (14km)
     * 요금: 1250(기본요금) + 100(추가요금) + 1100(3호선, 경유노선최대요금)
     *      일반: 2450원
     *      청소년: 1680원
     *      어린이: 1050원
     ***********************************************************************
     * **교대역에서~ 양재역으로 갈 경우**
     * 최단거리: 교대역-남부터미널역-양재역     (거리 합: 12, 소요시간: 10, 요금: 1350+1100 = 2450원)
     *      일반: 2450원
     *      청소년: 1680원
     *      어린이: 1050원
     * 최단시간: 교대역-강남역-양재역         (거리 합: 16, 소요시간: 6, 요금: 1350(최단거리에 따름)+2000 = 3350원)
     *      일반: 3350원
     *      청소년: 2400원
     *      어린이: 1500원
     *
     * **교대역에서~ 삼성역으로 갈 경우**
     * 최단거리 = 최단시간                 (거리 합: 16, 소요시간: 6, 요금: 1450+900 = 2350원)
     *      일반: 2350원
     *      청소년: 1600원
     *      어린이: 1000원
     *
     * **강남역에서~ 대치역으로 갈 경우**
     * 최단거리 = 최단시간                  (거리 합: 10, 소요시간: 8, 요금: 1250(기본요금)+2000 = 3250원)
     *      일반: 3250원
     *      청소년: 2320원
     *      어린이: 1450원
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        삼성역 = 지하철역_생성_요청("삼성역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        대치역 = 지하철역_생성_요청("대치역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 8, 3, 이호선노선요금);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 9, 2, 신분당선노선요금);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 6, 5, 삼호선노선요금);

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 삼성역, 8, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 6, 5));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(양재역, 대치역, 2, 5));
    }

    @DisplayName("강남역에서~ 남부터미널역으로 06:00(202202200600) 출발해서 갈 경우")
    @Test
    void findPath_0() {

    }

    @DisplayName("로그인한 성인 유저가 두 역의 최소시간 경로를 조회한다.")
    @Test
    void findPath_1() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_ADULT);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최소_시간_경로_조회를_요청(accessToken, 강남역, 남부터미널역, time);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 양재역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 15, 7, 3350)
        );
    }

    @DisplayName("로그인한 성인 유저가 두 역의 최단거리 경로를 조회한다.")
    @Test
    void findPath_2() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_ADULT);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최단_거리_경로_조회를_요청(accessToken, 강남역, 남부터미널역);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 교대역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 14, 8, 2450)
        );
    }

    @DisplayName("로그인 하지 않은 유저가 두 역의 최소시간 경로를 조회한다.")
    @Test
    void findPath_3() {
        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최소_시간_경로_조회를_요청(invalidToken, 강남역, 남부터미널역, time);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 양재역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 15, 7, 3350)
        );
    }

    @DisplayName("로그인 하지 않은 유저가 두 역의 최단거리 경로를 조회한다.")
    @Test
    void findPath_4() {
        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최단_거리_경로_조회를_요청(invalidToken, 강남역, 남부터미널역);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 교대역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 14, 8, 2450)
        );
    }

    @DisplayName("로그인한 청소년 유저가 두 역의 최소시간 경로를 조회한다.")
    @Test
    void findPath_5() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_YOUTH);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최소_시간_경로_조회를_요청(accessToken, 강남역, 남부터미널역, time);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 양재역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 15, 7, 2400)
        );
    }

    @DisplayName("로그인한 청소년 유저가 두 역의 최단거리 경로를 조회한다.")
    @Test
    void findPath_6() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_YOUTH);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최단_거리_경로_조회를_요청(accessToken, 강남역, 남부터미널역);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 교대역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 14, 8, 1680)
        );
    }

    @DisplayName("로그인한 어린이 유저가 두 역의 최소시간 경로를 조회한다.")
    @Test
    void findPath_7() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_CHILD);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최소_시간_경로_조회를_요청(accessToken, 강남역, 남부터미널역, time);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 양재역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 15, 7, 1500)
        );
    }

    @DisplayName("로그인한 어린이 유저가 두 역의 최단거리 경로를 조회한다.")
    @Test
    void findPath_8() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE_CHILD);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 유저가_두_역의_최단_거리_경로_조회를_요청(accessToken, 강남역, 남부터미널역);

        // then
        assertAll(
            () -> 경로조회의_결과_경로가_예상과_같다(response, 강남역, 교대역, 남부터미널역),
            () -> 경로조회의_결과_정보가_예상과_같다(response, 14, 8, 1050)
        );
    }
}
