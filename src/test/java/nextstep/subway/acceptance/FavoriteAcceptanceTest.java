package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성됨;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성을_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "member@email.com";
    public static final String PASSWORD = "password";

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

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2);

        SectionRequest sectionRequest = SectionRequest.of(남부터미널역, 양재역, 3, 3);
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, sectionRequest);

        사용자 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 남부터미널역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(findResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        LineRequest lineRequest = LineRequest.of(name, color, upStation, downStation, distance, duration);
        return LineSteps.지하철_노선_생성_요청(관리자, lineRequest).jsonPath().getLong("id");
    }

}