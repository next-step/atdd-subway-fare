package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class FareAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

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

        이호선 = 지하철_노선_생성_요청(관리자, "2호선", "green", 교대역, 강남역, 10, 1);
        신분당선 = 지하철_노선_생성_요청(관리자, "신분당선", "red", 강남역, 양재역, 10, 2, 900);
        삼호선 = 지하철_노선_생성_요청(관리자, "3호선", "orange", 교대역, 남부터미널역, 2, 3);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, 남부터미널역, 양재역, 3, 4);
    }

    @DisplayName("청소년의 경우 요금 계산 시 350원을 공제한 금액의 20%할인 된다.")
    @Test
    void getFareTeenager() {
        // given
        String 청소년 = 로그인_되어_있음("teenager@email.com", "password");

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(청소년, 교대역, 양재역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(720);
    }

    @DisplayName("어린이의 경우 요금 계산 시 350원을 공제한 금액의 50%할인 된다.")
    @Test
    void getFareChildren() {
        // given
        String 어린이 = 로그인_되어_있음("children@email.com", "password");

        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(어린이, 교대역, 양재역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(450);
    }


    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(String token, Long source, Long target) {
        return given(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type=distance", source, target)
                .then().log().all().extract();
    }

}
