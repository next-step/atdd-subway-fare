package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.AcceptanceTest;
import nextstep.subway.member.MemberSteps;
import nextstep.subway.path.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.path.acceptance.PathUtils.*;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 13;

    Long 교대역;
    Long 강남역;
    Long 양재역;
    Long 남부터미널역;
    Long 이호선;
    Long 신분당선;
    Long 삼호선;

    /**          (40km, 40min)
     * 교대역    --- *2호선* ---   강남역
     *   |                          |
     * (33km, 33min)             (40km, 40min)
     * *3호선*                   *신분당선*
     *   |                          |
     * 남부터미널역  --- *3호선* --- 양재
     *               (33km, 33min)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 40, 40, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 40, 40, 900);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 33, 33, 1100);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 33, 33));

    }

    @ParameterizedTest(name = "두 역의 최소 경로를 조회한다. [{arguments}]")
    @EnumSource(PathType.class)
    void findPath(PathType type) {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, type);

        // then
        경로_조회_성공(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @ParameterizedTest(name = "두 역의 최소 경로를 조회한다 - 로그인(청소년) - [{arguments}]")
    @EnumSource(PathType.class)
    void findPathWithLogin(PathType type) {
        // given
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_응답);
        String token = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 경로_조회_응답 = 두_역의_경로_조회를_요청(교대역, 양재역, type, token);

        // then
        경로_조회_성공_로그인(경로_조회_응답, Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }
}
