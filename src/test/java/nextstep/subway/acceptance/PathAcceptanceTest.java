package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fixture.MockMember;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.fixture.MockMember.CHILD;
import static nextstep.fixture.MockMember.TEENAGER;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 십호선;
    private Long 공항철도선;
    private Long 구호선;
    private Long 양재시민의숲역;
    private Long 역삼역;
    private Long 연신내역;
    private Long 계양역;
    private Long 김포공항역;

    /**
     * 무료 노선
     * |            (10, 10)                (2, 1)
     * 교대역    --- *2호선* ---   강남역 --- *2호선* --- 역삼역
     * |                           |                     |
     * *3호선*                 *신분당선*                10호선
     * (2, 2)                  (10, 10)               (10, 10)
     * |                           |                     |
     * 남부터미널역  --- *3호선* --- 양재역 --- *3호선* --- 양재시민의숲역
     * |              (3,5)                  (1, 2)
     * |
     * *3호선*
     * (55,55)
     * |
     * |                 (1,2)
     * 연신내역 --- *공항철도선_1200*  ---  김포공항역
     * |
     * *9호선_900*
     * (2,2)
     * |
     * 계양역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청(관리자, "양재시민의숲역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청(관리자, "역삼역").jsonPath().getLong("id");
        연신내역 = 지하철역_생성_요청(관리자, "연신내역").jsonPath().getLong("id");
        계양역 = 지하철역_생성_요청(관리자, "계양역").jsonPath().getLong("id");
        김포공항역 = 지하철역_생성_요청(관리자, "김포공항역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 2);
        십호선 = 지하철_노선_생성_요청("10호선", "yellow", 역삼역, 양재시민의숲역, 10, 10);
        공항철도선 = 지하철_노선_생성_요청("공항철도선", "sky-blue", 연신내역, 김포공항역, 1, 2, 1200);
        구호선 = 지하철_노선_생성_요청("9호선", "deep-blue", 연신내역, 계양역, 2, 2, 900);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 이호선, createSectionCreateParams(강남역, 역삼역, 2, 1));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(양재역, 양재시민의숲역, 1, 2));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, createSectionCreateParams(남부터미널역, 연신내역, 55, 55));
    }


    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        final JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(5),
                () -> assertThat(jsonPath.getInt("duration")).isEqualTo(7),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(1250)
        );
    }

    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(양재역, 역삼역);

        // then
        final JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).containsExactly(양재역, 강남역, 역삼역),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(12),
                () -> assertThat(jsonPath.getInt("duration")).isEqualTo(11),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(1350)
        );
    }

    @DisplayName("추가요금이 있는 노선을 이용할 경우 가장 높은 추가 요금이 적용된다.")
    @Test
    void findPathSurchargeByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(계양역, 김포공항역);

        // then
        final JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).containsExactly(계양역, 연신내역, 김포공항역),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(3),
                () -> assertThat(jsonPath.getInt("duration")).isEqualTo(4),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(2450)
        );
    }

    @DisplayName("어린이 요금이 적용 된다.")
    @Test
    void findPathByChild() {
        // when
        ExtractableResponse<Response> response = 로그인후_최단_시간_경로_조회를_요청(CHILD, 교대역, 남부터미널역);

        // then
        final JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(2),
                () -> assertThat(jsonPath.getInt("duration")).isEqualTo(2),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(450)
        );
    }

    @DisplayName("청소년 요금이 적용 된다.")
    @Test
    void findPathByTeenager() {
        // when
        ExtractableResponse<Response> response = 로그인후_최단_시간_경로_조회를_요청(TEENAGER, 교대역, 남부터미널역);

        // then
        final JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역),
                () -> assertThat(jsonPath.getInt("distance")).isEqualTo(2),
                () -> assertThat(jsonPath.getInt("duration")).isEqualTo(2),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(720)
        );
    }


    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return getShortestPathByType(source, target, PathType.DISTANCE);
    }

    private ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return getShortestPathByType(source, target, PathType.DURATION);
    }

    private ExtractableResponse<Response> 로그인후_최단_시간_경로_조회를_요청(MockMember member, Long source, Long target) {
        String accessToken = 로그인_되어_있음(member);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", PathType.DURATION)
                .when().get("/paths")
                .then().log().all().extract();
    }


    private ExtractableResponse<Response> getShortestPathByType(Long source, Long target, PathType type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", type)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int surcharge) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("surcharge", surcharge + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

}
