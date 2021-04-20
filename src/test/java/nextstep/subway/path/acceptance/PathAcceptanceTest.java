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

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    private final static int DEFAULT_FARE = 1_250;
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10, 0);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2, 10, 0);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 고속터미널역, 3, 20);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        String email = "gpwls@gmail.com";
        String password = "1234";
        int age = 100;

        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        회원_생성됨(memberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(email, password);
        ExtractableResponse<Response> response = 로그인_후_두_역_최단_경로_조회_요청(양재역.getId(),교대역.getId(), "DISTANCE", tokenResponse);

        // then
        총_거리와_소요시간을_함께_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 5, 20);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {

        // when
        String email = "gpwls@gmail.com";
        String password = "1234";
        int age = 100;

        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        회원_생성됨(memberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(email, password);
        ExtractableResponse<Response> response = 로그인_후_두_역_최단_경로_조회_요청(교대역.getId(),양재역.getId(), "DISTANCE", tokenResponse);

        // then
        총_거리와_소요시간을_함께_응답(response, Lists.newArrayList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()), 5, 20);
    }

    @DisplayName("지하철 경로 요금조회")
    @Test
    void findPathByDistanceWithFare() {
        //when
        String email = "gpwls@gmail.com";
        String password = "1234";
        int age = 100;

        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        회원_생성됨(memberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(email, password);
        ExtractableResponse<Response> response = 로그인_후_두_역_최단_경로_조회_요청(양재역.getId(),교대역.getId(), "DISTANCE", tokenResponse);

        //then
        경로_응답됨(response);
        총_거리와_소요시간을_함께_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 5, 20);
        요금_조회_함께_응답(response, DEFAULT_FARE);
    }

    @DisplayName("추가요금을 포함하는 지하철 경로 요금조회")
    @Test
    void findPathByDistanceWithFareAndSpecialFee() {
        //when
        String email = "gpwls@gmail.com";
        String password = "1234";
        int age = 100;

        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        회원_생성됨(memberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(email, password);
        ExtractableResponse<Response> response = 로그인_후_두_역_최단_경로_조회_요청(남부터미널역.getId(),고속터미널역.getId(), "DISTANCE", tokenResponse);

        //then
        경로_응답됨(response);
        총_거리와_소요시간을_함께_응답(response, Lists.newArrayList(남부터미널역.getId(), 양재역.getId(), 고속터미널역.getId()), 6, 30);
        요금_조회_함께_응답(response, DEFAULT_FARE);
    }

    @DisplayName("로그인 후 최단 경로 조회")
    @Test
    void findPathWithLoginUser() {
        String email = "gpwls@gmail.com";
        String password = "1234";
        int age = 10;

        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(email, password, age);
        회원_생성됨(memberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(email, password);

        //when
        ExtractableResponse<Response> response = 로그인_후_두_역_최단_경로_조회_요청(양재역.getId(), 교대역.getId(), "DISTANCE", tokenResponse);

        //then
        경로_응답됨(response);
        총_거리와_소요시간을_함께_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 5, 20);
        요금_조회_함께_응답(response, DEFAULT_FARE - ((DEFAULT_FARE - 350) * 50 / 100));
    }

}
