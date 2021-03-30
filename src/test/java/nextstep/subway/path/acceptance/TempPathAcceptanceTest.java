package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

public class TempPathAcceptanceTest extends AcceptanceTest {
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;
    private TokenResponse 어린이;
    private TokenResponse 청소년;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 29, 10, 100);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 29, 10, 500);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 30, 9, 0);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 30, 9);

        ExtractableResponse<Response> createChildResponse = 회원_생성_요청("child@child.com", "child", 8);
        회원_생성됨(createChildResponse);

        어린이 = 로그인_되어_있음("child@child.com", "child");

        ExtractableResponse<Response> createTeenagerResponse = 회원_생성_요청("teenager@teenager.com", "teenager", 17);
        회원_생성됨(createTeenagerResponse);

        청소년 = 로그인_되어_있음("teenager@teenager.com", "teenager");
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 강남역.getId(), 교대역.getId()), 58, 20, 2650);
    }

    @DisplayName("어린이로 로그인한 상태에서 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance_withChildToken() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(어린이, 양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 강남역.getId(), 교대역.getId()), 58, 20, 1150);
    }

    @DisplayName("청소년으로 로그인한 상태에서 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance_withTeenagerToken() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(청소년, 양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 강남역.getId(), 교대역.getId()), 58, 20, 1840);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 60, 18, 2250);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration_withChildToken() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(어린이, 양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 60, 18, 950);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration_withTeenagerToken() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(청소년, 양재역.getId(), 교대역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(양재역.getId(), 남부터미널역.getId(), 교대역.getId()), 60, 18, 1520);
    }
}

