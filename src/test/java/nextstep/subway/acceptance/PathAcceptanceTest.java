package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.PathLookUpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "a@email.com";
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

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 1, 5);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 10, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 0);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 10));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - Guest")
    @Test
    void findPathByDistanceWhenGuest() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DISTANCE);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 로그인한 유저가 19세 이상의 경우 할인 금액을 적용하지 않는다.")
    @Test
    void findPathByDistanceWhenAgeMoreThan19() {
        // when
        String accessToken = 특정_나이의_회원_생성_및_액세스_토큰_반환(19);
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DISTANCE, accessToken);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    private static String 특정_나이의_회원_생성_및_액세스_토큰_반환(int age) {
        회원_생성_요청(EMAIL, PASSWORD, age);
        return 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 로그인한 유저가 13세 이상 19세 미만인 경우 350원 공제 후 20% 할인 금액을 적용한다.")
    @Test
    void findPathByDistanceAgeBetween13And19() {
        // when
        String accessToken = 특정_나이의_회원_생성_및_액세스_토큰_반환(13);
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DISTANCE, accessToken);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
        assertThat(pathResponse.getFare()).isEqualTo(720);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 로그인한 유저가 6세 이상 13세 미만인 경우 350원 공제 후 50% 할인 금액을 적용한다.")
    @Test
    void findPathByDistanceAgeBetween6And13() {
        // when
        String accessToken = 특정_나이의_회원_생성_및_액세스_토큰_반환(6);
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DISTANCE, accessToken);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
        assertThat(pathResponse.getFare()).isEqualTo(450);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(11);
        assertThat(pathResponse.getFare()).isEqualTo(1450 + 10);
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다. - 환승역에 추가요금이 부과된 경우 더 큰 요금만 적용된다.")
    @Test
    void findPathByDurationAndExtraFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역, PathLookUpType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(11);
        assertThat(pathResponse.getFare()).isEqualTo(1450 + 10);
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, PathLookUpType type) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type.name())
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target, PathLookUpType type, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type.name())
                .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int fare) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("fare", fare + "");

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
}
