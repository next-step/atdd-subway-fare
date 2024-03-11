package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.SubwaySteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 테스트")
public class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /* 교대역   ---   강남역
         |             |
       남부터미널역 --- 양재역
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = 지하철_역_생성("교대역").jsonPath().getLong("id");
        강남역 = 지하철_역_생성("강남역").jsonPath().getLong("id");
        양재역 = 지하철_역_생성("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철_역_생성("남부터미널역").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성(new LineRequest("이호선", "green", 교대역, 강남역, 10L, 3L)).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성(new LineRequest("신분당선", "red", 강남역, 양재역, 14L, 5L)).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성(new LineRequest("삼호선", "orange", 양재역, 교대역, 23L, 10L)).jsonPath().getLong("id");
        지하철_구간_생성(삼호선, new SectionRequest(양재역, 남부터미널역, 5L, 6L));
    }

    /**
     * given 환승역으로 이어진 노선을 3개 생성하고
     * when 출발역과 도착역 정보로 최단 시간 경로를 조회하면
     * then 출발역과 도착역 사이의 최단 시간 경로 정보를 응답한다.
     */
    @DisplayName("최단 시간 경로 조회")
    @Test
    void shortest_time_path() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회(교대역, 양재역, PathType.DURATION);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(24L);
        assertThat(response.jsonPath().getLong("duration")).isEqualTo(8L);
    }


    /**
     * given 환승역으로 이어진 노선을 3개 생성하고
     * when 출발역과 도착역 정보로 최단 거리 경로를 조회하면
     * then 출발역과 도착역 사이의 최단 거리 경로 정보를 응답한다.
     */
    @DisplayName("최단 거리 경로 조회")
    @Test
    void shortest_distance_path() {
        // when
        ExtractableResponse<Response> response = 지하철_최단경로_조회(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.jsonPath().getLong("distance")).isEqualTo(23L);
        assertThat(response.jsonPath().getLong("duration")).isEqualTo(10L);
    }

    /**
     * given 환승역으로 이어진 노선을 3개 생성하고
     * when 출발역과 도착역을 같은 정보로 최단 거리 경로를 조회하면
     * then 최단경로 조회 에러가 발생한다.
     */
    @DisplayName("에러_최단 거리 경로 조회_출발역과 도착역 같음")
    @Test
    void shortestPath_error_source_target_same() {
        // when
        // then
        지하철_최단경로_조회_BAD_REQUEST(교대역, 교대역, PathType.DISTANCE);
    }

    /**
     * given 환승역으로 이어진 노선을 3개와 연결되지 않은 노선 1개를 생성하고
     * when 연결되어 있지 않은 출발역과 도착역 정보로 최단 경로를 조회하면
     * then 최단경로 조회 에러가 발생한다.
     */
    @DisplayName("에러_최단 경로 조회_출발역과 도착역이 연결되어 있지 않음")
    @Test
    void shortestPath_error_source_target_not_connected() {
        // given
        Long 미금역 = 지하철_역_생성("미금역").jsonPath().getLong("id");
        Long 정자역 = 지하철_역_생성("정자역").jsonPath().getLong("id");
        Long 분당선 = 지하철_노선_생성(new LineRequest("분당선", "yellow", 미금역, 정자역, 15L, 8L)).jsonPath().getLong("id");

        // when
        // then
        지하철_최단경로_조회_BAD_REQUEST(교대역, 미금역, PathType.DISTANCE);
    }

    /**
     * given 환승역으로 이어진 노선을 3개를 생성하고
     * when 존재하지 않는 출발역 혹은 도착역 정보로 최단 경로를 조회하면
     * then 최단경로 조회 에러가 발생한다.
     */
    @DisplayName("에러_최단 경로 조회_출발역과 도착역을 찾을 수 없음")
    @Test
    void shortestPath_error_source_target_not_found() {
        // when
        // then
        지하철_최단경로_조회_BAD_REQUEST(교대역, 0L, PathType.DISTANCE);
    }
}
