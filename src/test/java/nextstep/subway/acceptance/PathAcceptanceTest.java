package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.RestAssuredUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.step.TokenSteps.일반_로그인_요청;
import static nextstep.auth.acceptance.step.TokenSteps.토큰_추출;
import static nextstep.member.acceptance.step.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.step.LineSteps.지하철_노선을_생성한다;
import static nextstep.subway.acceptance.step.PathStep.*;
import static nextstep.subway.acceptance.step.SectionSteps.지하철_노선_구간을_등록한다;
import static nextstep.subway.acceptance.step.StationStep.지하철역을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String DISTANCE = "DISTANCE";
    private static final String DURATION = "DURATION";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final double DISCOUNT_RATE_FOR_TEENAGER = 0.8;
    public static final double DISCOUNT_RATE_FOR_CHILDREN = 0.5;
    public static final int BASIC_FARE = 350;

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

        교대역 = 지하철역을_생성한다("교대역").jsonPath().getLong("id");
        강남역 = 지하철역을_생성한다("강남역").jsonPath().getLong("id");
        양재역 = 지하철역을_생성한다("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역을_생성한다("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선을_생성한다("2호선", "green", 교대역, 강남역, 10, 1, 0).jsonPath().getLong("id");
        신분당선 = 지하철_노선을_생성한다("신분당선", "red", 강남역, 양재역, 10, 1, 0).jsonPath().getLong("id");
        삼호선 = 지하철_노선을_생성한다("3호선", "orange", 교대역, 남부터미널역, 2, 10, 0).jsonPath().getLong("id");

        지하철_노선_구간을_등록한다(삼호선, 남부터미널역, 양재역, 3, 12);
    }

    /**
     * Scenario : 두 역의 최단 거리 경로를 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 노선을 생성하고
     * And : 하나의 지하철 노선에 1개의 구간을 추가한 후
     * When : 최단 거리 경로 조회를 요청하면
     * Then : 경로와 거리, 총 소요 시간을 응답한다.
     */
    @DisplayName("최단 거리 경로 조회")
    @Test
    void searchShortestDistancePath() {
        // when
        ExtractableResponse<Response> pathsResponse = 경로_조회_요청(1, 3, DISTANCE, RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_이름_목록_검증(pathsResponse, 3, "교대역", "남부터미널역", "양재역");
        경로_응답_검증(pathsResponse, 5, 22, 1250);
    }

    /**
     * Scenario : 두 역의 최소 시간 경로를 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 노선을 생성하고
     * And : 하나의 지하철 노선에 1개의 구간을 추가한 후
     * When : 최소 시간 경로 조회를 요청하면
     * Then : 경로와 거리, 총 소요 시간을 응답한다.
     */
    @DisplayName("최소 시간 경로 조회")
    @Test
    void searchShortestDurationPath() {
        // when
        ExtractableResponse<Response> pathsResponse = 경로_조회_요청(1, 3, DURATION, RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_이름_목록_검증(pathsResponse, 3, "교대역", "강남역", "양재역");
        경로_응답_검증(pathsResponse, 20, 2, 1450);
    }

    /**
     * Scenario : 추가 요금이 있는 노선 조회
     * Given : 지하철역을 4개 생성하고
     * And : 각각 500원, 900원의 추가요금이 있는 노선을 2개 생성하고
     * When : 최소 시간 경로 조회를 요청하면
     * Then : 900원의 요금이 추가된 지하철 요금을 응답한다.
     */
    @DisplayName("추가 요금이 있는 노선의 경로 조회")
    @Test
    void additionalFeePath() {
        // given
        Long 가양역 = 지하철역을_생성한다("가양역").jsonPath().getLong("id");
        Long 마곡나루 = 지하철역을_생성한다("마곡나루").jsonPath().getLong("id");
        Long 계양 = 지하철역을_생성한다("계양").jsonPath().getLong("id");

        지하철_노선을_생성한다("9호선", "brown", 가양역, 마곡나루, 5, 1, 500);
        지하철_노선을_생성한다("공항철도", "blue", 마곡나루, 계양, 5, 1, 900);

        // when
        ExtractableResponse<Response> pathsResponse = 경로_조회_요청(가양역, 계양, DISTANCE, RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_이름_목록_검증(pathsResponse, 3, "가양역", "마곡나루", "계양");
        경로_응답_검증(pathsResponse, 10, 2, 2150);
    }

    /**
     * Scenario : 청소년이 경로 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 노선을 생성하고
     * And : 하나의 지하철 노선에 1개의 구간을 추가하고
     * And : 13세 이상 ~ 19세 미만의 청소년 계정을 생성하고
     * And : 로그인 요청을 한 후
     * When : 토큰을 이용해 최단 거리 경로 조회를 요청하면
     * Then : 350원을 제외한 나머지 금액의 20% 할인된 가격을 응답한다.
     */
    @DisplayName("청소년이 경로를 조회")
    @Test
    void searchPathByTeenager() {
        // given
        int age = 18;
        회원_생성_요청(EMAIL, PASSWORD, age);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> pathsResponse = 경로_조회_요청(accessToken, 1, 3, DISTANCE, RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_이름_목록_검증(pathsResponse, 3, "교대역", "남부터미널역", "양재역");
        경로_응답_검증(pathsResponse, 5, 22, BASIC_FARE + (int) Math.ceil(900 * DISCOUNT_RATE_FOR_TEENAGER));
    }

    /**
     * Scenario : 어린이가 경로 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 노선을 생성하고
     * And : 하나의 지하철 노선에 1개의 구간을 추가한 후
     * And : 6세 이상 ~ 13세 미만의 어린이 계정을 생성하고
     * And : 로그인 요청을 한 후
     * When : 최단 거리 경로 조회를 요청하면
     * Then : 350원에 350원을 제외한 나머지 금액의 50% 할인된 가격을 더한 요금을 응답한다.
     */
    @DisplayName("어린이가 경로를 조회")
    @Test
    void searchPathByChildren() {
        // given
        int age = 12;
        회원_생성_요청(EMAIL, PASSWORD, age);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> pathsResponse = 경로_조회_요청(accessToken, 1, 3, DISTANCE, RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_이름_목록_검증(pathsResponse, 3, "교대역", "남부터미널역", "양재역");
        경로_응답_검증(pathsResponse, 5, 22, 800);
    }
}
