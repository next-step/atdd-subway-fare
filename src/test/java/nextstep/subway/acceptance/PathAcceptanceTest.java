package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL_13 = "email_13@email.com";
    public static final String EMAIL_6 = "email_6@email.com";
    public static final String PASSWORD = "password";
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 논현역;
    private Long 신논현역;
    private Long 논현선;
    private Long 개방역;
    private Long 수리역;
    private Long 사호선;
    private Long 오호선;

    /**
     * 교대역    --- *2호선* ---   강남역     ---  4호선  ---   개방역
     * |                        |                           /
     * *3호선*                   *신분당선*                   5호선
     * |                        |                           /
     * 남부터미널역  --- *3호선* ---   양재                     수리역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        논현역 = 지하철역_생성_요청("논현역").jsonPath().getLong("id");
        신논현역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");

        개방역 = 지하철역_생성_요청("개방역").jsonPath().getLong("id");
        수리역 = 지하철역_생성_요청("수리역").jsonPath().getLong("id");


        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 15, 10, 0);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 58, 10, 0);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5, 0);
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 2));

        논현선 = 지하철_노선_생성_요청("논현선", "orange", 논현역, 신논현역, 58, 5, 0);

        사호선 = 지하철_노선_생성_요청("사호선", "orange", 강남역, 개방역, 10, 5, 1000);
        오호선 = 지하철_노선_생성_요청("오호선", "orange", 개방역, 수리역, 10, 5, 10000);

        회원_생성_요청(EMAIL_13, PASSWORD, 13);
        회원_생성_요청(EMAIL_6, PASSWORD, 6);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE", "DISTANCE");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    private ExtractableResponse<Response> 두_역의_경로_조회를_요청(Long source, Long target, String type, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(
                        identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 로그인한_사용자가_두_역의_경로_조회를_요청(Long source, Long target, String type, String accessToken, String identifier) {
        return RestAssured
                .given(spec).log().all()
                .filter(document(
                                identifier,
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("extraFare", extraFare + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최소 시간 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void test1() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DURATION", "DURATION");

        // then
        JsonPath jsonPath = response.jsonPath();
        List<Long> 경로 = jsonPath.getList("stations.id", Long.class);
        int 걸리는_시간 = response.jsonPath().getInt("duration");
        assertThat(경로).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(걸리는_시간).isEqualTo(7);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(5km)(10KM 이내)(기본운임 1,250원)")
    @Test
    void test2() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 양재역, "DISTANCE", "FARE_5");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(15km)(10km초과 ∼ 50km까지)(5km마다 100원)")
    @Test
    void test3() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(교대역, 강남역, "DISTANCE", "FARE_15");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }

    /**
     * Feature: 지하철 경로 검색
     *
     * Scenario: 두 역의 최단 거리 경로를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(58km)(10km초과 ∼ 50km까지)(8km마다 100원)")
    @Test
    void test4() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(논현역, 신논현역, "DISTANCE", "FARE_58");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(논현역, 신논현역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

    /**
     * Feature: 노선 추가 요금
     *
     * Scenario: 추가 요금이 있는 노선의 경비를 조회
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청
     * Then 최소 거리 기준으로 경로를 응답
     * And 지하철 이용 요금도 추가요금을 포함하여 응답
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(1250원 + 1000원)")
    @Test
    void test5() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(강남역, 개방역, "DISTANCE", "FARE");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 개방역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
    }

    /**
     * Feature: 노선 추가 요금
     *
     * Scenario: 추가요금이 있는 노선을 두개 이상 지날 경우 가장 높은 금액의 추가 요금만 적용
     *
     * Given 지하철 역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청
     * Then 최소 거리 기준으로 경로를 응답
     * And 지하철 이용 요금도 추가요금을 포함하여 응답
     */
    @DisplayName("두 역의 최소 거리 경로와 지하철 요금을 조회한다.(1250원 + 10000원)")
    @Test
    void test6() {
        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(강남역, 수리역, "DISTANCE", "FARE");

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 개방역, 수리역);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(11450);
    }


    /**
     * Feature: 로그인 한 사용자의 요금 계산
     *
     * Scenario: 로그인 한 사용자(13세)가 2km 를 이동했을 때의 요금을 계산한다.
     *
     * Given 지하철 역(교대역, 남부터미널역)이 등록되어 있음
     * And 지하철 노선(3호선)이 등록되어 있음
     * And 13세의 사용자가 등록되어 있음
     * And 로그인하여 사용자의 인증 토큰을 들고 있음
     * When 경로 조회를 요청
     * Then 청소년(13세 이상, 19세미만) 요금이 적용된 지하철 이용 요금이 응답됨
     */
    @DisplayName("13살의 사용자가 로그인 했을 때의 지하철 요금을 조회한다.")
    @Test
    void test7() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL_13, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> pathResponse = 로그인한_사용자가_두_역의_경로_조회를_요청(교대역, 남부터미널역, "DISTANCE", accessToken, "FARE");

        // then
        assertThat(pathResponse.jsonPath().getInt("fare")).isEqualTo(1070);
    }

    /**
     * Featrue: 로그인 한 사용자의 요금 계산
     *
     * Scenario: 로그인 한 사용자(6세)가 2km 를 이동했을 때의 요금을 계산한다.
     *
     * Given 지하철 역(교대역, 남부터미널역)이 등록되어 있음
     * And 지하철 노선(3호선)이 등록되어 있음
     * And 6세의 사용자가 등록되어 있음
     * And 로그인하여 사용자의 인증 토큰을 들고 있음
     * When 경로 조회를 요청
     * Then 어린이(6세 이상, 13세 미만) 요금이 적용된 지하철 이용 요금이 응답됨
     */
    @DisplayName("6살의 사용자가 로그인 했을 때의 지하철 요금을 조회한다.")
    @Test
    void test8() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL_6, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> pathResponse = 로그인한_사용자가_두_역의_경로_조회를_요청(교대역, 남부터미널역, "DISTANCE", accessToken, "FARE");

        // then
        assertThat(pathResponse.jsonPath().getInt("fare")).isEqualTo(800);
    }

    /**
     * Feature: 로그인 한 사용자의 복합 요금 계산
     *
     * Scenario: 로그인 한 사용자(13세)가 교대역-강남역-개방역(25km)를 이동했을 때의 요금을 계산한다.
     *
     * Given 지하철 역(교대역, 강남역, 개방역)이 등록되어 있음
     * And 지하철 노선(2호선, 4호선-추가요금 1000원)이 등록되어 있음
     * And 13세의 사용자가 등록되어 있음
     * And 로그인하여 사용자의 인증 토큰을 들고 있음
     * When 경로 조회를 요청
     * Then 청소년(13세 이상, 19세미만) 요금과 추가운임(10km초과 ~ 50km까지 5km마다 100원)이 적용된 지하철 이용 요금이 응답됨
     */
    @DisplayName("로그인 한 사용자(13세)가 25km를 이동한 후 노선 추가요금 1000원을 계산했을 때의 요금을 계산한다.")
    @Test
    void test9() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL_13, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> pathResponse = 로그인한_사용자가_두_역의_경로_조회를_요청(교대역, 개방역, "DISTANCE", accessToken, "FARE");

        // then
        assertThat(pathResponse.jsonPath().getInt("fare")).isEqualTo(2110);
    }

}
