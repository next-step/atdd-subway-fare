package nextstep.subway.acceptance.path;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.annotation.AcceptanceTest;
import nextstep.subway.acceptance.line.LineApiRequester;
import nextstep.subway.acceptance.section.SectionApiRequester;
import nextstep.subway.acceptance.station.StationApiRequester;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.SectionCreateRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.utils.JsonPathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 구간 경로 조회 관련 기능")
@AcceptanceTest
public class PathAcceptanceTest {

    Long 교대역id;
    Long 강남역id;
    Long 양재역id;
    Long 남부터미널역id;
    Long 이호선id;
    Long 신분당선id;
    Long 삼호선id;

    @BeforeEach
    void setUp() {
        교대역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("교대역"));
        강남역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("강남역"));
        양재역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("양재역"));
        남부터미널역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("남부터미널역"));

        LineCreateRequest 이호선 = new LineCreateRequest("2호선", "green", 교대역id, 강남역id, 10, 10);
        이호선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(이호선));

        LineCreateRequest 신분당선 = new LineCreateRequest("신분당선", "red", 강남역id, 양재역id, 10, 10);
        신분당선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(신분당선));

        LineCreateRequest 삼호선 = new LineCreateRequest("3호선", "orange", 교대역id, 남부터미널역id, 2, 3);
        삼호선id = JsonPathUtil.getId(LineApiRequester.createLineApiCall(삼호선));

        SectionCreateRequest 남부터미널양재역 = new SectionCreateRequest(남부터미널역id, 양재역id, 3, 3);
        SectionApiRequester.generateSection(남부터미널양재역, 삼호선id);
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 경로를 조회하면
     * Then 출발역과 도착역까지의 최단 길이의 경로를 조회한다
     */
    @DisplayName("출발역과 도착역까지의 최단 길이 경로 조회")
    @Test
    void showDistanceShortestPaths() {
        //when
        ExtractableResponse<Response> response = PathApiRequester.getDistanceShortestPath(교대역id, 양재역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> ids = response.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId).collect(Collectors.toList());
        int distance = response.jsonPath().getInt("distance");
        assertThat(ids).containsExactly(교대역id, 남부터미널역id, 양재역id);
        assertThat(distance).isEqualTo(5);
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 경로를 조회하면
     * Then 출발역과 도착역까지의 최단 시간의 경로를 조회한다
     */
    @DisplayName("출발역과 도착역까지의 최단 시간 경로 조회")
    @Test
    void showDurationShortestPaths() {
        //when
        ExtractableResponse<Response> response = PathApiRequester.getDurationShortestPath(교대역id, 양재역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> ids = response.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId).collect(Collectors.toList());
        int distance = response.jsonPath().getInt("duration");
        assertThat(ids).containsExactly(교대역id, 남부터미널역id, 양재역id);
        assertThat(distance).isEqualTo(6);
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 경로를 조회하면
     * Then 출발역과 도착역까지의 요금을 조회한다
     */
    @DisplayName("출발역과 도착역까지의 요금 조회")
    @Test
    void showPathFare() {
        //given
        Long 성수역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("성수역"));
        SectionCreateRequest 양재성수역 = new SectionCreateRequest(양재역id, 성수역id, 10, 3);
        SectionApiRequester.generateSection(양재성수역, 신분당선id);

        //when
        ExtractableResponse<Response> response = PathApiRequester.getDistanceShortestPath(교대역id, 성수역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        int fare = response.jsonPath().getInt("fare");
        assertThat(fare).isEqualTo(1450);
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 길이가 50 이상인 경로를 조회하면
     * Then 출발역과 도착역까지의 추가된 요금을 조회한다
     */
    @DisplayName("출발역과 도착역까지의 요금 조회")
    @Test
    void surchargeTest() {
        //given
        Long 성수역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("성수역"));
        SectionCreateRequest 양재성수역 = new SectionCreateRequest(양재역id, 성수역id, 100, 3);
        SectionApiRequester.generateSection(양재성수역, 신분당선id);

        //when
        ExtractableResponse<Response> response = PathApiRequester.getDistanceShortestPath(교대역id, 성수역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        int fare = response.jsonPath().getInt("fare");
        assertThat(fare).isEqualTo(2850);
    }


    /**
     * Given 지하철 구간을 등록하고
     * When 출발역과 도착역을 같게하여 경로를 조회하면
     * Then 예외가 발생한다
     */
    @DisplayName("출발역과 도착역을 같게하여 경로를 조회")
    @Test
    void showSameAsSourceStationAndTargetStation() {
        //when
        ExtractableResponse<Response> response = PathApiRequester.getDistanceShortestPath(교대역id, 교대역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("출발역과 도착역이 같습니다.");
    }

    /**
     * Given 지하철 구간을 등록하고
     * When 연결되어있지 않은 출발역과 도착역의 경로를 조회하면
     * Then 예외가 발생한다
     */
    @DisplayName("연결되어있지 않은 출발역과 도착역의 경로를 조회")
    @Test
    void showDisConnectedStation() {
        //given
        Long 서울역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("서울역"));
        Long 명동역id = JsonPathUtil.getId(StationApiRequester.createStationApiCall("명동역"));
        LineCreateRequest 사호선 = new LineCreateRequest("4호선", "sky", 서울역id, 명동역id, 2, 3);
        JsonPathUtil.getId(LineApiRequester.createLineApiCall(사호선));

        //when
        ExtractableResponse<Response> response = PathApiRequester.getDistanceShortestPath(교대역id, 서울역id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asPrettyString()).isEqualTo("연결되어있지 않은 출발역과 도착역의 경로는 조회할 수 없습니다.");
    }
}
