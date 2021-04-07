package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.path.steps.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    private TokenResponse token;

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10, 500);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2, 10, 900);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
        
        // 로그인
        token = 유효한_토큰_생성됨(new MemberRequest(EMAIL, PASSWORD, 15));

    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 최단거리")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(token, 양재역.getId(), 교대역.getId());

        // then
        최단_거리_경로를_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 최소시간")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(token, 교대역.getId(), 양재역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(교대역.getId(), 강남역.getId(), 양재역.getId()), 20, 20);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 최단거리 + 요금조회 + 추가정책(노선별) + 추가정책(연령별)")
    @Test
    void findPathByDistanceWithFareAddedAgePolicy() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(token, 양재역.getId(), 교대역.getId());

        // then
        최단_거리_경로를_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()));
        총_거리와_소요_시간을_함께_응답함(response, 5, 20);
        지하철_이용_요금도_함께_응답함(response, 1440); // (int) Math.floor(((1250 + 900) - 350) * 0.8)
    }

    @DisplayName("최단거리 요금 조회 + 비로그인 상태")
    @Test
    void findPathByDistanceWithFareByNonLoginMember() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(new TokenResponse("none"), 양재역.getId(), 교대역.getId());

        // then
        최단_거리_경로를_응답(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()));
        총_거리와_소요_시간을_함께_응답함(response, 5, 20);
        지하철_이용_요금도_함께_응답함(response, 2150);
    }

}
