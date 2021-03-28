package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    public static final String ADULT_EMAIL = "adult@email.com";
    public static final String YOUTH_EMAIL = "youth@email.com";
    public static final String KID_EMAIL = "kid@email.com";
    public static final String PASSWORD = "password";

    public static final int ADULT_AGE = 22;
    public static final int YOUTH_AGE = 17;
    public static final int KID_AGE = 9;

    TokenResponse adultTokenResponse;
    TokenResponse youthTokenResponse;
    TokenResponse kidTokenResponse;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성_요청(ADULT_EMAIL, PASSWORD, ADULT_AGE);
        회원_생성_요청(YOUTH_EMAIL, PASSWORD, YOUTH_AGE);
        회원_생성_요청(KID_EMAIL, PASSWORD, KID_AGE);


        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 요금이_추가된_지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10, 0);
        신분당선 = 요금이_추가된_지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10, 10, 900);
        삼호선 = 요금이_추가된_지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2, 10, 500);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //given
        adultTokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 5, 20);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        //given
        adultTokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청( 교대역.getId(), 양재역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()), 5, 20);
    }


    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void managePathFinder() {
        //when
        ExtractableResponse<Response> pathFindResponse = 두_역의_최단_거리_경로_조회를_요청( 양재역.getId(), 교대역.getId());

        //then
        경로_응답됨(pathFindResponse, Arrays.asList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 5, 20);

        경로_요금_일치함(pathFindResponse, 1750);

    }

    @DisplayName("추가 요금이 있는 노선을 경유하는 최단 거리 경로를 조회하여 운임을 계산한다.")
    @Test
    void costWithAddedCostLine() {
        //when
        ExtractableResponse<Response> pathFindResponse = 두_역의_최단_거리_경로_조회를_요청( 강남역.getId(), 양재역.getId());

        //then
        경로_응답됨(pathFindResponse, Arrays.asList(강남역.getId(), 양재역.getId()), 10, 10);
        경로_요금_일치함(pathFindResponse, 2150);
    }

    @DisplayName("어린이가 추가 요금이 있는 노선을 경유하는 최단 거리 경로를 조회하여 운임을 계산한다.")
    @Test
    void costWithAddedCostLineAndAdult() {
        //when
        adultTokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);
        ExtractableResponse<Response> pathFindResponse = 회원이_두_역의_최단_거리_경로_조회를_요청(adultTokenResponse, 강남역.getId(), 양재역.getId());

        //then
        경로_응답됨(pathFindResponse, Arrays.asList(강남역.getId(), 양재역.getId()), 10, 10);
        경로_요금_일치함(pathFindResponse, 2150);
    }

    @DisplayName("어린이가 추가 요금이 있는 노선을 경유하는 최단 거리 경로를 조회하여 운임을 계산한다.")
    @Test
    void costWithAddedCostLineAndKid() {
        //when
        kidTokenResponse = 로그인_되어_있음(KID_EMAIL, PASSWORD);
        ExtractableResponse<Response> pathFindResponse = 회원이_두_역의_최단_거리_경로_조회를_요청(kidTokenResponse, 강남역.getId(), 양재역.getId());

        //then
        경로_응답됨(pathFindResponse, Arrays.asList(강남역.getId(), 양재역.getId()), 10, 10);
        경로_요금_일치함(pathFindResponse, 1250);
    }

    @DisplayName("청소년이 추가 요금이 있는 노선을 경유하는 최단 거리 경로를 조회하여 운임을 계산한다.")
    @Test
    void costWithAddedCostLineAndYouth() {
        //when
        youthTokenResponse = 로그인_되어_있음(YOUTH_EMAIL, PASSWORD);
        ExtractableResponse<Response> pathFindResponse = PathSteps.회원이_두_역의_최단_거리_경로_조회를_요청(youthTokenResponse, 강남역.getId(), 양재역.getId());

        //then
        경로_응답됨(pathFindResponse, Arrays.asList(강남역.getId(), 양재역.getId()), 10, 10);
        경로_요금_일치함(pathFindResponse, 1790);
    }
}
