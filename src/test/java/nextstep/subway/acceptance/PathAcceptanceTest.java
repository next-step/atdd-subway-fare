package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.PathSearchType;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 성수역;
    private Long 합정역;
    private Long 양재역;
    private Long 공덕역;
    private Long 서울역;
    private Long 남부터미널역;
    private Long 종로3가역;
    private Long 동대문역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 육호선;
    private Long 일호선;
    private Long 오호선;
    private Long 공항철도선;

    /**
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     *
     *     |·········· 10 ··········|··············· 20 ···············|················· 25 ·················|
     *   교대역 ───── *2호선* ───── 강남역 ────────── *2호선* ────────── 성수역 ──────────── *2호선* ──────────── 합정역 ···
     *     │                        │                                                                         │     ·
     *   *3호선*                 *신분당선*                                                                   *6호선*  8 (+500)
     *     │                        │                                                                         │     ·
     * 남부터미널역 ── *3호선* ───── 양재역                                     ·····서울역 ───── *공항철도* ───── 공덕역 ···
     *                                                                     ·       │ \····· 10 (+900) ·····/
     *                                                                     ·       │
     *                                                                  5 (+300) *1호선*
     *                                                                     ·       │
     *                                                                     ·       │
     *                                                                     ····종로3가역 ─── *5호선* ─── 동대문역
     *                                                                                \··· 5 (+400) ···/
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        성수역 = 지하철역_생성_요청("성수역").jsonPath().getLong("id");
        합정역 = 지하철역_생성_요청("합정역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        공덕역 = 지하철역_생성_요청("공덕역").jsonPath().getLong("id");
        서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        종로3가역 = 지하철역_생성_요청("종로3가역").jsonPath().getLong("id");
        동대문역 = 지하철역_생성_요청("동대문역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 4);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 4);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 5);
        육호선 = 추가_요금이_있는_지하철_노선_생성_요청("6호선", "brown", 합정역, 공덕역, 8, 3, 500);
        공항철도선 = 추가_요금이_있는_지하철_노선_생성_요청("공항철도선", "lightblue", 공덕역, 서울역, 10, 5, 900);
        일호선 = 추가_요금이_있는_지하철_노선_생성_요청("1호선", "blue", 서울역, 종로3가역, 5, 2, 300);
        오호선 = 추가_요금이_있는_지하철_노선_생성_요청("5호선", "purple", 종로3가역, 동대문역, 5, 3, 400);

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 성수역, 20, 10));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(성수역, 합정역, 25, 12));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 5));
    }

    /**
     * When 출발역에서 도착역까지의 최단 거리 기준으로 경로 조회를 요청하면
     * Then 최단 거리 기준 경로를 총 거리, 소요 시간, 지하철 요금과 함께 응답한다.
     */
    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(5),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(10),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250)
        );
    }

    /**
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
     * Then 최소 시간 기준 경로를 총 거리, 소요 시간, 지하철 요금과 함께 응답한다.
     */
    @DisplayName("두 역의 최단 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(20),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(8),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_450)
        );
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 이내라면
     * Then 지하철 요금은 기본 요금이다.
     */
    @DisplayName("지하철 구간 길이가 10km 이내라면, 지하철 요금은 기본 요금이다.")
    @Test
    void basicDistanceFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 강남역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 초과, 50km 이내라면
     * Then 요금 계산 시, 기본 요금에 5km 마다 100원씩 요금이 추가된다.
     */
    @DisplayName("지하철 구간 길이가 10km 초과 50km 이하라면, 기본 요금에 5km 마다 100원씩 요금이 추가된다.")
    @Test
    void middleDistanceFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 성수역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_650);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 50km 초과라면
     * Then 요금 계산 시, 기본 요금에 8km 마다 100원씩 요금이 추가된다.
     */
    @DisplayName("지하철 구간 길이가 50km 초과라면, 기본 요금에 8km 마다 100원씩 요금이 추가된다.")
    @Test
    void longDistanceFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 합정역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_850);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 이내이고, 추가 요금이 있는 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 노선의 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 10km 이내이고 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 노선 추가 요금) 이다.")
    @Test
    void basicDistanceWithExtraFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(합정역, 공덕역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_750);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 이내이고, 추가 요금이 있는 여러 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 가장 큰 노선 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 10km 이내이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 가장 큰 노선 추가 요금) 이다.")
    @Test
    void basicDistanceWithExtraFares() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(서울역, 동대문역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_650);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 초과 50km 이내이고, 추가 요금이 있는 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 노선 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 10km 초과 50km 이내이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 가장 큰 노선 추가 요금) 이다.")
    @Test
    void middleDistanceWithExtraFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(성수역, 공덕역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_250);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 10km 초과 50km 이내이고, 추가 요금이 있는 여러 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 가장 큰 노선 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 10km 초과 50km 이내이고 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(5km 당 100원) + 노선 추가 요금) 이다.")
    @Test
    void middleDistanceWithExtraFares() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(성수역, 서울역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_850);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 50km 초과이고, 추가 요금이 있는 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 노선 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 50km 초과이고 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 노선 추가 요금) 이다.")
    @Test
    void longDistanceWithExtraFare() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(강남역, 공덕역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_350);
    }

    /**
     * When 출발역에서 도착역까지의 구간 길이가 50km 초과이고, 추가 요금이 있는 여러 노선을 이용하면
     * Then 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 가장 큰 노선 추가 요금) 이다.
     */
    @DisplayName("구간 길이가 50km 초과이고 여러 추가 요금 노선이 포함되었다면, 총 지하철 요금은 (기본 요금 + 추가 요금(8km 당 100원) + 가장 큰 노선 추가 요금) 이다.")
    @Test
    void longDistanceWithExtraFares() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(강남역, 동대문역);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_950);
    }

    /**
     * Given 성인 사용자가 로그인을 요청하고
     * When 지하철 경로 조회를 요청한 로그인 사용자가 성인이라면
     * Then 지하철 요금은 할인되지 않은 요금이다.
     */
    @DisplayName("로그인 사용자가 성인이라면, 지하철 요금은 할인되지 않은 요금이다.")
    @Test
    void findPathByAdultMember() {
        // given
        String accessToken = 베어러_인증_로그인_요청("adult@email.com", "password").jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 강남역, accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_250);
    }

    /**
     * Given 청소년 사용자가 로그인을 요청하고
     * When 지하철 경로 조회를 요청한 로그인 사용자가 청소년이라면
     * Then 지하철 요금은 운임에서 350원을 공제한 금액의 20%가 할인된 요금이다.
     */
    @DisplayName("로그인 사용자가 청소년이라면, 지하철 요금은 운임에서 350원을 공제한 금액의 20%가 할인된 요금이다.")
    @Test
    void findPathByTeenagerMember() {
        // given
        String accessToken = 베어러_인증_로그인_요청("teenager@email.com", "password").jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 강남역, accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(800);
    }

    /**
     * Given 어린이 사용자가 로그인을 요청하고
     * When 지하철 경로 조회를 요청한 로그인 사용자가 어린이이라면
     * Then 지하철 요금은 운임에서 350원을 공제한 금액의 50%가 할인된 요금이다.
     */
    @DisplayName("로그인 사용자가 어린이라면, 지하철 요금은 운임에서 350원을 공제한 금액의 50%가 할인된 요금이다.")
    @Test
    void findPathByChildMember() {
        // given
        String accessToken = 베어러_인증_로그인_요청("child@email.com", "password").jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(교대역, 강남역, accessToken);

        // then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_070);
    }

    private ExtractableResponse<Response> 로그인_사용자가_두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DISTANCE)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DISTANCE)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 두_역의_최단_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathSearchType}", source, target, PathSearchType.DURATION)
            .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 추가_요금이_있는_지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    private Long 추가_요금이_있는_지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int extraFare) {
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
}
