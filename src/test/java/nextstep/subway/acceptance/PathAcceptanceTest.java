package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private static final int DEFAULT_SURCHARGE = 0;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10   , 1)* ---   강남역
     * |                                |
     * *3호선(2, 10)*                     *신분당선* (10, 1)
     * |                                |
     * 남부터미널역  --- *3호선(3,   5)* ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 20, 1, DEFAULT_SURCHARGE);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 20, 1, DEFAULT_SURCHARGE);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 200);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. 총 거리 7, 기본요금 + 삼호선추가요금 200 : 1450")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450)
        );
    }

    @DisplayName("두 역의 최단 시간 경로를 조회한다.(거리 40 , 최단거리 5, 예상요금 1250원)")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);
        // then
//        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);

        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    /**
     * given
     * when 로그인 후 경로를 조회하면
     * then 청소년 요금할인이 적용된 요금을 확인 할 수 있다.
     * (1450 - 350) * 0.8 : 880
     */
    @DisplayName("청소년으로 로그인 후 경로를 조회하면 할인 금액을 확인 할 수 있다.")
    @Test
    void findPathAndSurchargeWithLogin(){
        String email = "teenager@gmail.com";
        String password = "123";
        회원_생성_요청(email, password, 15);
        String 청소년 = 로그인_되어_있음(email, password);
        // when
        ExtractableResponse<Response> response = 로그인_두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, 청소년);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(880)
        );
    }

    /**
     * given
     * when 로그인 후 경로를 조회하면
     * then 청소년 요금할인이 적용된 요금을 확인 할 수 있다.
     * (1450 - 350) * 0.5 : 550
     */
    @DisplayName("어린이로 로그인 후 경로를 조회하면 할인 금액을 확인 할 수 있다.")
    @Test
    void findPathAndSurchargeWithLogin2(){
        String email = "child@gmail.com";
        String password = "123";
        회원_생성_요청(email, password, 7);
        String 어린이 = 로그인_되어_있음(email, password);
        // when
        ExtractableResponse<Response> response = 로그인_두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, 어린이);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(550)
        );
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance
            , int duration, int surCharge) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("surCharge", surCharge + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }


}
