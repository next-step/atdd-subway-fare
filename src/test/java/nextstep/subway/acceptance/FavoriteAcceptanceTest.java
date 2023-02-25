package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.FavoriteSteps.권한없어서_실패됨;
import static nextstep.subway.acceptance.FavoriteSteps.역이_등록되어_있지_않아서_실패됨;
import static nextstep.subway.acceptance.FavoriteSteps.요청값_누락으로_실패됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제대상_없음;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_없음;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";
    private static final String 로그인하지_않은_토큰 = "test";
    private static final Long 존재하지않음 = 99L;

    private Long 신분당선;
    private Long 이호선;
    private Long 삼호선;
    private Long 강남역;
    private Long 양재역;
    private Long 교대역;
    private Long 남부터미널역;
    private String 사용자;

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

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));

        사용자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        즐겨찾기_생성됨(즐겨찾기_생성_응답, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(사용자, 즐겨찾기);

        즐겨찾기_조회됨(즐겨찾기_조회_응답, 강남역, 양재역);

        final ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(사용자, 즐겨찾기);

        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("즐겨찾기 요청 역이 누락되어서 저장에 실패한다.")
    @Test
    void error_inputValue_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, null, 양재역);

        요청값_누락으로_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("로그인하지 않은 사용자는 즐겨찾기 저장에 실패한다.")
    @Test
    void error_noAuth_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인하지_않은_토큰, 강남역, 양재역);

        권한없어서_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("등록되지 않은 역은 즐겨찾기 저장에 실패한다.")
    @Test
    void error_noRegisterStation_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 존재하지않음, 양재역);

        역이_등록되어_있지_않아서_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("로그인하지 않은 사용자는 즐겨찾기 조회에 실패한다.")
    @Test
    void error_showFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(로그인하지_않은_토큰, 즐겨찾기);

        권한없어서_실패됨(즐겨찾기_조회_응답);
    }

    @DisplayName("존재하지 않는 즐겨찾기는 조회할 수 없다")
    @Test
    void error_noFavorite_showFavorite() {

        즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(사용자, 존재하지않음);

        즐겨찾기_조회_없음(즐겨찾기_조회_응답);
    }

    @DisplayName("로그인하지 않은 사용자는 즐겨찾기를 제거한다.")
    @Test
    void error_removeFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(로그인하지_않은_토큰, 즐겨찾기);

        권한없어서_실패됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("존재하지 않는 즐겨찾기는 제거할 수 없다")
    @Test
    void error_noFavorite_removeFavorite() {

        즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_삭제_요청(사용자, 존재하지않음);

        즐겨찾기_삭제대상_없음(즐겨찾기_조회_응답);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}