package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 정자역;
    private Long 모란역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(0원, 5km, 10분)* ---     강남역
     * |                                               |
     * *3호선(500원, 6km, 8분)*                    *신분당선(1000원, 6km, 12분)*
     * |                                               |
     * 남부터미널역  --- *3호선(500원, 7km, 12분)* ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청(관리자, "정자역").jsonPath().getLong("id");
        모란역 = 지하철역_생성_요청(관리자, "모란역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 0, 5, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 1_000, 6, 12);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 500, 6, 8);

        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, SectionRequest.of(남부터미널역, 양재역, 7, 12));
    }

    /**
     * Given 지하철역이 등록되어있음
     * And 로그인하지 않음
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답합
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(13),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_850)
        );
    }

    /**
     * 교대역    --- *2호선*(0원, 5km, 10분)  --------   강남역
     * |                                                  |
     * *3호선* (500원, 6km, 8분)                     *신분당선*(1000원, 6km, 12분) --  양재역  -- *신분당선*(1000원, 20km, 20분) --  정자역
     * |
     * 남부터미널역  --- *3호선*(500원, 7km, 12분) ---   양재역  -- *3호선*(500원, 5km, 5분) --  모란역  --  *3호선*(500원, 8km, 8분)  -- 정자역
     *
     * Given 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * And(add) 지하철 노선에 추가적으로 구간을 등록한다.
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("역의 개수와 상관 없이 소요 시간이 적은 경로를 조회하기")
    @Test
    void findPathByShortestDuration() {
        // and(add)
        지하철_노선에_지하철_구간_생성_요청(관리자, 신분당선, SectionRequest.of(양재역, 정자역, 20, 20));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, SectionRequest.of(양재역, 모란역, 5, 5));
        지하철_노선에_지하철_구간_생성_요청(관리자, 삼호선, SectionRequest.of(모란역, 정자역, 8, 8));

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 정자역);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역, 모란역, 정자역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(26),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(33),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(2_150)
        );
    }

    /**
     *  *팔호선* (2000원, 100km, 100분) --- 양재역
     * |
     * 교대역    --- *2호선*(0원, 5km, 10분)  --------  강남역
     * |                                                 |
     * *3호선* (500원, 6km, 8분)                         *신분당선*(1000원, 6km, 12분) --  양재역
     * |
     * 남부터미널역  --- *3호선*(500원, 7km, 12분) --- 양재역
     *
     * Given 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * And(add) 새로운 지하철 노선을 추가하고
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("역이 1개지만 시간이 너무 오래걸릴 경우 다른 경로로 조회함")
    @Test
    void findMinimumDurationPath() {
        // and(add)
        Long 팔호선 = 지하철_노선_생성_요청("팔호선", "purple", 교대역, 양재역, 0,100, 100);

        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(13),
                () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(20),
                () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1_850)
        );
    }


    /**
     * 교대역    --- *2호선(0원, 5km, 10분)*    ---   강남역    ---    *신분당선(1000원, 6km, 10분)*   ---   선릉역
     * |                                               |
     * *3호선(500원, 6km, 8분)*                    *신분당선(1000원, 6km, 12분)*
     * |                                               |
     * 남부터미널역  --- *3호선(500원, 7km, 12분)* ---  양재
     *
     * Given 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * And(add) 새로운 지하철 노선을 추가하고
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답합
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("최소 시간 경로 조회 시 환승했을 때 추가 요금이 가장 비싼 것으로 채택 됨")
    @Test
    void findTransferFare() {
        // Given(add)
        // when
        // then
    }

    /**
     * 교대역    --- *2호선(0원, 5km, 10분)* ---     강남역
     * |                                               |
     * *3호선(500원, 6km, 8분)*                    *신분당선(1000원, 6km, 12분)*
     * |                                               |
     * 남부터미널역  --- *3호선(500원, 7km, 12분)* ---  양재
     *
     * Given 어린이 유저로 로그인한다.
     * And 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답합
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("로그인 한 어린이 유저는 요금 할인 혜택을 제공받는다")
    @Test
    void findPathChildrenUserFare() {
        // given
        // when
        // then
    }

    /**
     * Given 청소년 유저로 로그인한다.
     * And 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("로그인 한 청소년 유저는 요금 할인 헤택을 제공받는다")
    @Test
    void findPathTeenagerUserFare() {
        // given
        // when
        // then
    }

    /**
     * Given 성인 유저로 로그인한다.
     * And 지하철역이 등록되어있다.
     * And 추가 요금이 붙은 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어 있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     * And 지하철 이용 요금도 함께 응답함
     */
    @DisplayName("로그인 한 성인 유저는 요금 할인 헤택을 제공 받지 못한다")
    @Test
    void findPathAdultUserFare() {
        // given
        // when
        // then
    }

    private ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all().extract();
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int price, int distance, int duration) {
        LineRequest lineRequest = LineRequest.of(name, color, upStation, downStation, price, distance, duration);
        return LineSteps.지하철_노선_생성_요청(관리자, lineRequest).jsonPath().getLong("id");
    }
}
