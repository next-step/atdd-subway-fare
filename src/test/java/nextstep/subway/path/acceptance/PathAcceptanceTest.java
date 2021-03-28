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
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.path.constant.PathConstant.DEFAULT_FARE;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String 어린이_이메일 = "email1@email.com";
    private static final String 청소년_이메일 = "email2@email.com";
    private static final String 비밀번호 = "1234";

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    @BeforeEach
    @DisplayName("지하철역과 회원 등록")
    public void setUp() {
        super.setUp();
        모든_지하철역_등록되어_있음();
        청소년과_어린이_회원_등록되어_있음();
    }

    private void 모든_지하철역_등록되어_있음() {
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10, 10, 900);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2, 10, 100);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    private void 청소년과_어린이_회원_등록되어_있음() {
        회원_생성_요청(청소년_이메일, 비밀번호, 13);
        회원_생성_요청(어린이_이메일, 비밀번호, 6);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역.getId(), 양재역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()), 5, 20);
    }

    @DisplayName("두 역의 최소 소요 시간 거리 경로를 조회한다")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(교대역.getId(), 양재역.getId());

        // then
        경로_응답됨(response, Lists.newArrayList(교대역.getId(), 강남역.getId(), 양재역.getId()), 20, 20);
    }

    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가")
    @Test
    void findPathByDurationWithAdditionalPareOfLine() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(
            남부터미널역.getId(),
            양재역.getId()
        );

        // then
        경로와_요금_응답됨(
            response,
            Lists.newArrayList(
                남부터미널역.getId(),
                양재역.getId()
            ),
            3,
            10,
            DEFAULT_FARE + 삼호선.getAdditionalFare()
        );
    }

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
    @Test
    void findPathByDurationWithMaxFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(교대역.getId(), 양재역.getId());

        // then
        경로와_요금_응답됨(
            response,
            Lists.newArrayList(
                교대역.getId(),
                강남역.getId(),
                양재역.getId()
            ),
            20,
            20,
            DEFAULT_FARE + 200 + 신분당선.getAdditionalFare()
        );
    }

    @DisplayName("청소년 요금으로 계산")
    @Test
    void findPathByDurationWithYouth() {
        // given
        TokenResponse tokenResponse = 로그인_되어_있음(청소년_이메일, 비밀번호);

        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(
            교대역.getId(),
            양재역.getId(),
            tokenResponse
        );

        // then
        int sumFare = DEFAULT_FARE + 신분당선.getAdditionalFare() + 200;
        경로와_요금_응답됨(
            response,
            Lists.newArrayList(
                교대역.getId(),
                강남역.getId(),
                양재역.getId()
            ),
            20,
            20,
            sumFare - (int)((sumFare - 350) * 0.2)
        );
    }

    @DisplayName("어린이 요금으로 계산")
    @Test
    void findPathByDurationWithChild() {
        // given
        TokenResponse tokenResponse = 로그인_되어_있음(어린이_이메일, 비밀번호);

        // when
        ExtractableResponse<Response> response = 두_역의_최소_소요_시간_경로_조회를_요청(
            교대역.getId(),
            양재역.getId(),
            tokenResponse
        );

        // then
        int sumFare = DEFAULT_FARE + 신분당선.getAdditionalFare() + 200;

        경로와_요금_응답됨(
            response,
            Lists.newArrayList(
                교대역.getId(),
                강남역.getId(),
                양재역.getId()
            ),
            20,
            20,
            sumFare - (int)((sumFare - 350) * 0.5)
        );
    }
}
