package nextstep.subway.maps.map.acceptance;

import static nextstep.subway.maps.line.acceptance.step.LineAcceptanceStep.*;
import static nextstep.subway.maps.line.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.maps.station.acceptance.step.StationAcceptanceStep.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.map.acceptance.step.PathAcceptanceStep;
import nextstep.subway.maps.station.dto.StationResponse;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {

    /**
     * 교대역      -      강남역
     * |                 |
     * 남부터미널역           |
     * |                 |
     * 양재역      -       -|
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED");
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE");

        Long lineId1 = createLineResponse1.as(LineResponse.class).getId();
        Long lineId2 = createLineResponse2.as(LineResponse.class).getId();
        Long lineId3 = createLineResponse3.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        Long stationId4 = createdStationResponse4.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(lineId1, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId1, stationId2, 2, 2);

        지하철_노선에_지하철역_등록되어_있음(lineId2, null, stationId2, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId2, stationId3, 2, 1);

        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 2, 2);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = PathAcceptanceStep.
            출발역에서_도착역까지_최단_또는_최소시간_경로조회_요청(1L, 3L, "DISTANCE");

        // then
        PathAcceptanceStep.총_거리와_소요시간을_함께_응답검증(response, 3, 4);
        PathAcceptanceStep.경로를_순서대로_정렬하여_응답검증(response, Arrays.asList(1L, 4L, 3L));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회할 때, 지하철 이용요금도 함께 응답된다.")
    @Test
    void findPathByDistanceWithFare() {
        // when
        ExtractableResponse<Response> response = PathAcceptanceStep.
            출발역에서_도착역까지_최단_또는_최소시간_경로조회_요청(1L, 3L, "DISTANCE");

        // then
        PathAcceptanceStep.총_거리와_소요시간을_함께_응답검증(response, 3, 4);
        PathAcceptanceStep.지하철_이용요금이_함께_응답검증(response);
        PathAcceptanceStep.경로를_순서대로_정렬하여_응답검증(response, Arrays.asList(1L, 4L, 3L));
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response =
            PathAcceptanceStep.출발역에서_도착역까지_최단_또는_최소시간_경로조회_요청(1L, 3L, "DURATION");

        // then
        PathAcceptanceStep.총_거리와_소요시간을_함께_응답검증(response, 4, 3);
        PathAcceptanceStep.경로를_순서대로_정렬하여_응답검증(response, Arrays.asList(1L, 2L, 3L));
    }
}
