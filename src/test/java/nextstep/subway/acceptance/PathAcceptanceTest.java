package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 상도역;
    private Long 천호역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 칠호선;
    private Long 팔호선;

    /**
     * 교대역    --- *2호선* ---   강남역  --- *7호선 (1110_000) * ---   상도역   --- *8호선 (110_000)* --- 천호역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath()
                .getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath()
                .getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath()
                .getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath()
                .getLong("id");
        상도역 = 지하철역_생성_요청(관리자, "상도역").jsonPath()
                .getLong("id");
        천호역 = 지하철역_생성_요청(관리자, "천호역").jsonPath()
                .getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 7);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 5);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10);
        칠호선 = 지하철_노선_생성_요청_추가요금_포함("7호선", "darkgreen", 강남역, 상도역, 4, 10, 1110_000);
        팔호선 = 지하철_노선_생성_요청_추가요금_포함("8호선", "pink", 상도역, 천호역, 5, 10, 110_000);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath()
                .getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams)
                .jsonPath()
                .getLong("id");
    }

    private Long 지하철_노선_생성_요청_추가요금_포함(String name, String color, Long upStation, Long downStation, int distance, int duration, int additionalFare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionalFare", additionalFare + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams)
                .jsonPath()
                .getLong("id");
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
     * Given 지하철 역, 노선이 등록되어있고, 지하철 노선에 지하철역이 등록되어있을때,
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 응답하며, 총 거리와 소요 시간, 요금을 함께 응답한다.
     */
    @DisplayName("두 역의 최소 시간 기준 경로를 조회한다")
    @Test
    void findPathByDuration() {
        // When
        var response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath()
                        .getInt("distance")).isEqualTo(20),
                () -> assertThat(response.jsonPath()
                        .getInt("duration")).isEqualTo(12),
                () -> assertThat(response.jsonPath()
                        .getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(response.jsonPath()
                        .getInt("fare")).isEqualTo(1_450)
        );
    }

    /**
     * Given 지하철 역, 노선이 등록되어있고, 지하철 노선에 지하철역이 등록되어있을때,
     * When 없는 출발역과 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 에러가 발생한다.
     */
    @DisplayName("없는 출발 역과 도착역의 최소 시간 기준 경로를 조회한다")
    @Test
    void findPathByDuration_fail_no_exists_station() {
        // When
        Long 없는역ID = 999L;
        var response = 두_역의_최소_시간_경로_조회를_요청(없는역ID, 양재역);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 역, 노선이 등록되어있고, 지하철 노선에 추가요금이 있을때,
     * When 추가요금이 있는 지하철 노선을 지나는 경로를 요청하면
     * Then 기본요금에 추가요금이 포함되어 요금이 조회된다.
     */
    @DisplayName("추가요금 있는 노선을 지나는 최소 시간 기준 경로를 조회한다")
    @Test
    void findPathByDuration_with_additionalFare() {
        // When
        var response = 두_역의_최소_시간_경로_조회를_요청(강남역, 상도역);

        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath()
                        .getInt("distance")).isEqualTo(4),
                () -> assertThat(response.jsonPath()
                        .getInt("duration")).isEqualTo(10),
                () -> assertThat(response.jsonPath()
                        .getList("stations.id", Long.class)).containsExactly(강남역, 상도역),
                () -> assertThat(response.jsonPath()
                        .getInt("fare")).isEqualTo(1111_250)
        );
    }

    /**
     * Given 지하철 역, 노선이 등록되어있고, 지하철 노선에 추가요금이 있을때,
     * When 추가요금이 있는 지하철 노선을 지나며 환승하는 경로를 요청하면
     * Then 기본요금에 추가요금이 포함되어 요금이 조회된다.
     */
    @DisplayName("추가요금 있는 노선끼리 환승을 해야하는 최소 시간 기준 경로를 조회한다")
    @Test
    void findPathByDuration_with_additionalFare_transfer() {
        // When
        var response = 두_역의_최소_시간_경로_조회를_요청(강남역, 천호역);

        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath()
                        .getInt("distance")).isEqualTo(9),
                () -> assertThat(response.jsonPath()
                        .getInt("duration")).isEqualTo(20),
                () -> assertThat(response.jsonPath()
                        .getList("stations.id", Long.class)).containsExactly(강남역, 상도역, 천호역),
                () -> assertThat(response.jsonPath()
                        .getInt("fare")).isEqualTo(1111_250)
        );
    }

    /**
     * Given 어린이 연령의 로그인 사용자가 로그인 되어있고,
     * When 경로를 조회하면
     * Then 할인된 요금이 조회된다.
     */
    @DisplayName("어린이 연령의 로그인 사용자가 경로를 조회한다.")
    @Test
    void findPathByDistance_loginUser_children() {
        // Given
        String email = "children@email.com";
        String password = "password";
        String 어린이 = 로그인_되어_있음(email, password);

        // When
        var response = RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(어린이)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역)
                .queryParam("target", 강남역)
                .queryParam("type", "DISTANCE")
                .when()
                .get("/paths")
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.jsonPath()
                .getInt("fare")).isEqualTo(450);
    }

    /**
     * Given 청소년 연령의 로그인 사용자가 로그인되어있고,
     * When 경로를 조회하면
     * Then 할인된 요금이 조회된다.
     */
    @DisplayName("청소년 연령의 로그인 사용자가 경로를 조회한다.")
    @Test
    void findPathByDistance_loginUser_teenager() {
        // Given
        String email = "teenager@email.com";
        String password = "password";
        String 청소년 = 로그인_되어_있음(email, password);

        // When
        var response = RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(청소년)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 교대역)
                .queryParam("target", 강남역)
                .queryParam("type", "DISTANCE")
                .when()
                .get("/paths")
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.jsonPath()
                .getInt("fare")).isEqualTo(720);
    }
}