package nextstep.subway.acceptance.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.common.annotation.AcceptanceTest;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.acceptance.TokenSteps;
import nextstep.subway.acceptance.fixture.LineFixture;
import nextstep.subway.acceptance.fixture.SectionFixture;
import nextstep.subway.acceptance.fixture.StationFixture;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.path.PathSteps.로그인하지않은사용자_경로_조회요청;
import static nextstep.subway.acceptance.util.RestAssuredUtil.경로_조회_요청;
import static nextstep.subway.acceptance.util.RestAssuredUtil.생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AcceptanceTest
@DisplayName("지하철 경로 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PathAcceptanceTest {
    private ExtractableResponse<Response> 교대역;
    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 양재역;
    private ExtractableResponse<Response> 남부터미널역;
    private ExtractableResponse<Response> 이호선;
    private ExtractableResponse<Response> 신분당선;
    private ExtractableResponse<Response> 삼호선;
    private Long 교대역아이디;
    private Long 강남역아이디;
    private Long 양재역아이디;
    private Long 남부터미널역아이디;
    private String email;
    private String password;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void before() {
        교대역 = 생성_요청(
                StationFixture.createStationParams("교대역"),
                "/stations");
        강남역 = 생성_요청(
                StationFixture.createStationParams("강남역"),
                "/stations");
        양재역 = 생성_요청(
                StationFixture.createStationParams("양재역"),
                "/stations");
        남부터미널역 = 생성_요청(
                StationFixture.createStationParams("남부터미널역"),
                "/stations");
        교대역아이디 = 교대역.jsonPath().getLong("id");
        강남역아이디 = 강남역.jsonPath().getLong("id");
        양재역아이디 = 양재역.jsonPath().getLong("id");
        남부터미널역아이디 = 남부터미널역.jsonPath().getLong("id");
        이호선 = 생성_요청(
                LineFixture.createLineParams("2호선", "GREEN",
                        교대역아이디,
                        강남역아이디,
                        10L,
                        10L,
                        500L),
                "/lines");
        신분당선 = 생성_요청(
                LineFixture.createLineParams("신분당선",
                        "RED",
                        강남역아이디,
                        양재역아이디,
                        10L,
                        10L,
                        0L),
                "/lines");
        삼호선 = 생성_요청(
                LineFixture.createLineParams("3호선",
                        "ORANGE",
                        교대역아이디,
                        남부터미널역아이디,
                        2L,
                        10L,
                        900L),
                "/lines");
        생성_요청(
                SectionFixture.createSectionParams(남부터미널역아이디, 양재역아이디, 3L, 10L),
                "/lines/" + 삼호선.jsonPath().getLong("id") + "/sections"
        );
        email = "test@email.com";
        password = "password1234";
    }


    /**
     * given 어린이 회원을 생성하고 토큰을 조회한다.
     * when 최단거리를 어린이 회원 토큰으로 조회 요청
     * then 출발역과 마지막역 사이의 모든 역을 조회할 수 있다.
     */
    @Test
    @DisplayName("어린이 회원이 최단 거리 지하철 경로를 조회한다.")
    void 어린이_지하철_경로_조회한다() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 고속터미널역_신사역_경로_조회 = 경로_조회_요청("/paths",
                교대역아이디,
                양재역아이디,
                PathType.DISTANCE, accessToken);

        //then
        assertAll(
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getList("stations")).containsExactly(교대역.jsonPath().get(), 남부터미널역.jsonPath().get(), 양재역.jsonPath().get()),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("distance")).isEqualTo(5),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("duration")).isEqualTo(20),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getInt("fare")).isEqualTo(900)
        );
    }

    /**
     * given 청소년 회원을 생성하고 토큰을 조회한다.
     * when 최단거리를 청소년 회원 토큰으로 조회 요청
     * then 출발역과 마지막역 사이의 모든 역을 조회할 수 있다.
     */
    @Test
    @DisplayName("청소년 회원이 최단 거리 지하철 경로를 조회한다.")
    void 청소년_지하철_경로_조회한다() {
        //given
        MemberSteps.회원_생성_요청(email, password, 16);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 고속터미널역_신사역_경로_조회 = 경로_조회_요청("/paths",
                교대역아이디,
                양재역아이디,
                PathType.DISTANCE, accessToken);

        //then
        assertAll(
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getList("stations")).containsExactly(교대역.jsonPath().get(), 남부터미널역.jsonPath().get(), 양재역.jsonPath().get()),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("distance")).isEqualTo(5),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("duration")).isEqualTo(20),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getInt("fare")).isEqualTo(1440)
        );
    }

    /**
     * given 회원을 생성하고 토큰을 조회한다.
     * when 최단거리를 청소년 회원 토큰으로 조회 요청
     * then 출발역과 마지막역 사이의 모든 역을 조회할 수 있다.
     */
    @Test
    @DisplayName("최단 거리 지하철 경로를 조회한다.")
    void 지하철_경로_조회한다() {
        //given
        MemberSteps.회원_생성_요청(email, password, 22);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 고속터미널역_신사역_경로_조회 = 경로_조회_요청("/paths",
                교대역아이디,
                양재역아이디,
                PathType.DISTANCE, accessToken);

        //then
        assertAll(
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getList("stations")).containsExactly(교대역.jsonPath().get(), 남부터미널역.jsonPath().get(), 양재역.jsonPath().get()),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("distance")).isEqualTo(5),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("duration")).isEqualTo(20),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }

    /**
     * given 회원을 생성하고 토큰을 조회한다.
     * when 출발역과 마지막역을 동일한역으로 조회하면
     * then 에러가 발생한다.
     */
    @Test
    @DisplayName("출발역과 마지막역을 동일한 역으로 조회하면 에러가 발생한다.")
    void 출발역과_도착역이_같은_경로에러() {
        //given
        MemberSteps.회원_생성_요청(email, password, 13);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 조회_요청 = 경로_조회_요청("/paths", 교대역아이디, 교대역아이디, PathType.DISTANCE, accessToken);
        //then
        assertThat(조회_요청.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 서로 연결되어있지 않은 구간을 등록한다.
     * and 회원을 생성하고 토큰을 조회한다.
     * when 출발역과 마지막역을 구간에 연결되어있지 않은역으로 조회하면
     * then 에러가 발생한다.
     */
    @Test
    @DisplayName("출발역과 마지막역을 구간에 연결되어있지 역으로 조회하면 에러가 발생한다.")
    void 출발역과_도착역이_구간에_존재하지않으면_에러() {
        //given
        MemberSteps.회원_생성_요청(email, password, 13);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        ExtractableResponse<Response> 신림역 = 생성_요청(
                StationFixture.createStationParams("신림역"),
                "/stations");
        ExtractableResponse<Response> 보라매역 = 생성_요청(
                StationFixture.createStationParams("보라매역"),
                "/stations");
        ExtractableResponse<Response> 사당역 = 생성_요청(
                StationFixture.createStationParams("사당역"),
                "/stations");
        ExtractableResponse<Response> 이수역 = 생성_요청(
                StationFixture.createStationParams("이수역"),
                "/stations");

        생성_요청(
                LineFixture.createLineParams("신림선", "GRAY", 신림역.jsonPath().getLong("id"), 보라매역.jsonPath().getLong("id"), 10L, 10L, 500L),
                "/lines");
        생성_요청(
                LineFixture.createLineParams("4호선", "BLUE", 사당역.jsonPath().getLong("id"), 이수역.jsonPath().getLong("id"), 10L, 10L, 900L),
                "/lines");

        //when
        ExtractableResponse<Response> 조회_요청 = 경로_조회_요청("/paths", 신림역.jsonPath().getLong("id"), 사당역.jsonPath().getLong("id"), PathType.DISTANCE, accessToken);

        //then
        assertThat(조회_요청.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * given 회원을 생성하고 토큰을 조회한다.
     * when 출발역과 마지막역을 존재하지 않은역으로 조회하면
     * then 에러가 발생한다.
     */
    @Test
    @DisplayName("출발역과 마지막역을 존재하지않는 역으로 조회하면 에러가 발생한다.")
    void 출발역과_도착역이_존재하지않으면_에러() {
        //given
        MemberSteps.회원_생성_요청(email, password, 13);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 조회_요청 = 경로_조회_요청("/paths", 888L, 999L, PathType.DISTANCE, accessToken);
        //then
        assertThat(조회_요청.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("로그인하지 않은 사용자가 경로조회를 하면 기본 요금으로 조회가 된다.")
    void 로그인하지않은사용자_경로조회() {
        //given

        //when
        ExtractableResponse<Response> 고속터미널역_신사역_경로_조회 = 로그인하지않은사용자_경로_조회요청(교대역아이디, 양재역아이디, PathType.DISTANCE);

        //then
        assertAll(
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getList("stations")).containsExactly(교대역.jsonPath().get(), 남부터미널역.jsonPath().get(), 양재역.jsonPath().get()),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("distance")).isEqualTo(5),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getLong("duration")).isEqualTo(20),
                () -> assertThat(고속터미널역_신사역_경로_조회.jsonPath().getInt("fare")).isEqualTo(2150)
        );
    }
}
