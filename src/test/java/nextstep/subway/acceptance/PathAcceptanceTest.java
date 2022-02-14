package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.두_역의_최소_시간_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.지하철_노선_생성_요청;
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

    /**
     * 교대역    --- *2호선* ---   강남역
     *  |      거리:8, 시간:3       |
     *  |                          |
     * *3호선*                   *신분당선*
      거리:6, 시간:5             거리:8, 시간:3
        |                          |
     *  |                          |
     * 남부터미널역  --- *3호선* ---   양재
     *           거리:6, 시간:5
     *
     * **교대역에서~ 양재로 갈 경우**
     * 최단거리: 교대역-남부터미널역-양재역 (거리 합: 5, 시간 합: 10)
     * 최단시간: 교대역-강남역-양재역 (시간 합: 6, 거리 합: 20)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 8, 3);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 8, 3);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 6, 5);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 6, 5));
    }

    @DisplayName("두 역의 최소시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsExactly(교대역, 강남역, 양재역),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(6),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(16)
//            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1450)
        );
    }

    @DisplayName("두 역의 최단거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertAll(
            () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                .containsExactly(교대역, 남부터미널역, 양재역),
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(10),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(12)
//            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350)
        );
    }
}
