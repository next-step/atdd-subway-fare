package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static nextstep.common.error.SubwayError.NO_FIND_SAME_SOURCE_TARGET_STATION;
import static nextstep.common.error.SubwayError.NO_PATH_CONNECTED;
import static nextstep.common.error.SubwayError.NO_REGISTER_LINE_STATION;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.PathSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 검암역;
    private Long 몽촌토성역;
    private Long 부평역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 일호선;
    private String 최단거리  = SearchType.DURATION.name();
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

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        검암역 = 지하철역_생성_요청("검암역").jsonPath().getLong("id");
        몽촌토성역 = 지하철역_생성_요청("몽촌토성역").jsonPath().getLong("id");
        부평역 = 지하철역_생성_요청("부평역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 20);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 20);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 4);
        일호선 = 지하철_노선_생성_요청("1호선", "orange", 검암역, 몽촌토성역, 5, 10);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 6));
    }

    @DisplayName("최적의 경로를 찾고 요금도 계산한다.")
    @ParameterizedTest(name = "총 요금이 1250원인 경우 {0} 세는 {1} 원의 요금을 낸다.")
    @CsvSource(value = {"6,800", "12,800", "13,1070", "18,1070", "19,1250", "20,1250"})
    void findPathByDistance(final int age) {
        회원_생성_요청(EMAIL, PASSWORD, age);
        사용자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        final ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(사용자, 교대역, 양재역, 최단거리);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }

    @DisplayName("출발역과 도착역이 같아서 조회가 불가능합니다")
    @Test
    void error_showRoutes() {
        final ExtractableResponse<Response> 지하철_노선에_지하철_최적_경로_조회_응답 = 두_역의_경로_조회를_요청(사용자, 교대역, 교대역, 최단거리);

        final JsonPath jsonPathResponse = 지하철_노선에_지하철_최적_경로_조회_응답.response().body().jsonPath();
        assertAll(
                () -> assertThat(지하철_노선에_지하철_최적_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(jsonPathResponse.getString("message")).contains(NO_FIND_SAME_SOURCE_TARGET_STATION.getMessage())
        );
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않아서 조회가 불가능합니다")
    @Test
    void error_showRoutes_2() {
        final ExtractableResponse<Response> 지하철_노선에_지하철_최적_경로_조회_응답 = 두_역의_경로_조회를_요청(사용자, 교대역, 검암역, 최단거리);

        final JsonPath jsonPathResponse = 지하철_노선에_지하철_최적_경로_조회_응답.response().body().jsonPath();
        assertAll(
                () -> assertThat(지하철_노선에_지하철_최적_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(jsonPathResponse.getString("message")).contains(NO_PATH_CONNECTED.getMessage())
        );
    }

    @DisplayName("요청한 역이 노선의 등록되어 있지 않습니다")
    @Test
    void error_showRoutes_3() {
        final ExtractableResponse<Response> 지하철_노선에_지하철_최적_경로_조회_응답 = 두_역의_경로_조회를_요청(사용자, 교대역, 부평역, 최단거리);

        final JsonPath jsonPathResponse = 지하철_노선에_지하철_최적_경로_조회_응답.response().body().jsonPath();
        assertAll(
                () -> assertThat(지하철_노선에_지하철_최적_경로_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(jsonPathResponse.getString("message")).contains(NO_REGISTER_LINE_STATION.getMessage())
        );
    }
}
