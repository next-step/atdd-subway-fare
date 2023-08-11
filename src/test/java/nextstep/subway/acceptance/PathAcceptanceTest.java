package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineStep;
import nextstep.subway.acceptance.step.PathStep;
import nextstep.subway.acceptance.step.SectionStep;
import nextstep.subway.acceptance.step.StationStep;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.RestAssuredUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
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

        교대역 = StationStep.지하철역을_생성한다("교대역").jsonPath().getLong("id");
        강남역 = StationStep.지하철역을_생성한다("강남역").jsonPath().getLong("id");
        양재역 = StationStep.지하철역을_생성한다("양재역").jsonPath().getLong("id");
        남부터미널역 = StationStep.지하철역을_생성한다("남부터미널역").jsonPath().getLong("id");

        이호선 = LineStep.지하철_노선을_생성한다("2호선", "green", 교대역, 강남역, 10, 1).jsonPath().getLong("id");
        신분당선 = LineStep.지하철_노선을_생성한다("신분당선", "red", 강남역, 양재역, 10, 1).jsonPath().getLong("id");
        삼호선 = LineStep.지하철_노선을_생성한다("3호선", "orange", 교대역, 남부터미널역, 2, 10).jsonPath().getLong("id");

        SectionStep.지하철_노선_구간을_등록한다(삼호선, 남부터미널역, 양재역, 3, 12);
    }

    /**
     * Scenario : 두 역의 최단 거리 경로를 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 구간을 생성하고
     * And : 하나의 지하철역에 1개의 구간을 추가한 후
     * When : 최단 거리 경로 조회를 요청하면
     * Then : 경로와 거리, 총 소요 시간을 응답한다.
     */
    @DisplayName("최단 거리 경로 조회")
    @Test
    void searchShortestDistancePath() {
        // when
        ExtractableResponse<Response> pathsResponse = PathStep.경로_조회_요청(1, 3, "DISTANCE", RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 역_이름_목록_추출(pathsResponse);
        int distance = 총_이동거리_추출(pathsResponse);
        int duration = 소요시간_추출(pathsResponse);
        int fare = 이용_요금_추출(pathsResponse);

        assertThat(stationNames).hasSize(3);
        assertThat(stationNames).containsExactly("교대역", "남부터미널역", "양재역");
        assertThat(distance).isEqualTo(5);
        assertThat(duration).isEqualTo(22);
        assertThat(fare).isEqualTo(1250);
    }

    private List<String> 역_이름_목록_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getList("stations.name", String.class);
    }

    private int 총_이동거리_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("distance");
    }

    private int 소요시간_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("duration");
    }

    private int 이용_요금_추출(ExtractableResponse<Response> pathsResponse) {
        return pathsResponse.jsonPath().getInt("fare");
    }

    /**
     * Scenario : 두 역의 최소 시간 경로를 조회
     * Given : 지하철역을 4개 생성하고
     * And : 4개의 지하철역을 각각 2개씩 포함하는 3개의 구간을 생성하고
     * And : 하나의 지하철역에 1개의 구간을 추가한 후
     * When : 최소 시간 경로 조회를 요청하면
     * Then : 경로와 거리, 총 소요 시간을 응답한다.
     */
    @DisplayName("최소 시간 경로 조회")
    @Test
    void searchShortestDurationPath() {
        // when
        ExtractableResponse<Response> pathsResponse = PathStep.경로_조회_요청(1, 3, "DURATION", RestAssuredUtils.given_절_생성());

        // then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<String> stationNames = 역_이름_목록_추출(pathsResponse);
        int distance = 총_이동거리_추출(pathsResponse);
        int duration = 소요시간_추출(pathsResponse);
        int fare = 이용_요금_추출(pathsResponse);

        assertThat(stationNames).hasSize(3);
        assertThat(stationNames).containsExactly("교대역", "강남역", "양재역");
        assertThat(distance).isEqualTo(20);
        assertThat(duration).isEqualTo(2);
        assertThat(fare).isEqualTo(1250 + 200);
    }
}
