package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.FindPathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 역삼역;

    /**
     * 교대역    --- *2호선* ---   강남역  --- *2호선* ---역삼역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 1, 200);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10, 300);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3, 3));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(강남역, 역삼역, 6, 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        var response = PathSteps.두_역의_최단_경로_조회를_요청(교대역, 양재역, FindPathType.DISTANCE).as(PathResponse.class);

        // then
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(response.getFare()).isEqualTo(1550); // 기본요금 1250 + 3호선 추가요금 300원
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. 추가요금 확인")
    @Test
    void findPathByDistanceAdditionalFee() {
        // when
        var response = PathSteps.두_역의_최단_경로_조회를_요청(남부터미널역, 강남역, FindPathType.DISTANCE).as(PathResponse.class);

        // then
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(남부터미널역, 교대역, 강남역);
        assertThat(response.getFare()).isEqualTo(1650); // 기본요금 1250 + 3호선 추가요금 300원 + 거리 추가요금 100원
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. 추가요금 확인")
    @Test
    void findPathByDistanceAdditionalFee1() {
        // when
        var response = PathSteps.두_역의_최단_경로_조회를_요청(교대역, 역삼역, FindPathType.DISTANCE).as(PathResponse.class);

        // then
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(교대역, 강남역, 역삼역);
        assertThat(response.getFare()).isEqualTo(1650); // 기본요금 1250 + 2호선 추가요금 200원 + 거리 추가요금 200원
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        var response = PathSteps.두_역의_최단_경로_조회를_요청(교대역, 양재역, FindPathType.DURATION).as(PathResponse.class);

        // then
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(교대역, 강남역, 양재역);
    }


    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int additionalFee) {
        LineRequest lineRequest = new LineRequest(name, color, upStation, downStation, distance, duration, additionalFee);
        return LineSteps.지하철_노선_생성_요청(lineRequest).jsonPath().getLong("id");
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        LineRequest lineRequest = new LineRequest(name, color, upStation, downStation, distance, duration, 0);
        return LineSteps.지하철_노선_생성_요청(lineRequest).jsonPath().getLong("id");
    }

    private SectionRequest createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        return new SectionRequest(upStationId, downStationId, distance, duration);
    }
}
