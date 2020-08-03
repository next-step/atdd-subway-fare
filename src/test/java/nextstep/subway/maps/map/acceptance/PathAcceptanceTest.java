package nextstep.subway.maps.map.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static nextstep.subway.maps.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.maps.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.maps.map.acceptance.step.PathAcceptanceStep.*;
import static nextstep.subway.maps.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

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
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN", 900);
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
        지하철_노선에_지하철역_등록되어_있음(lineId2, stationId2, stationId3, 2, 1);

        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 2, 2);
    }

    @DisplayName("두 역의 최소 시간 경로와 요금을 조회한다.")
    @Test
    void findPathByDurationForFare() {
        ExtractableResponse<Response> response = 최단_경로를_조회한다(1L, 3L, PathType.DURATION);

        경로가_정상적으로_조회됨(response, 4, 3, 1250, Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceForFare() {
        ExtractableResponse<Response> response = 최단_경로를_조회한다(1L, 3L, PathType.DISTANCE);

        경로가_정상적으로_조회됨(response, 3, 4, 1250, Lists.newArrayList(1L, 4L, 3L));
    }

    @DisplayName("추가 운임이 있는 노선에서의 두 역의 최단 거리 경로를 조회하고 요금을 계산한다.")
    @Test
    void findPathWithExtraFare() {
        ExtractableResponse<Response> response = 최단_경로를_조회한다(1L, 2L, PathType.DISTANCE);

        경로가_정상적으로_조회됨(response, 2, 2, 2150, Lists.newArrayList(1L, 2L));
    }

    @DisplayName("추가 운임이 있는 노선에서의 두 역의 최단 거리 경로를 조회하고 요금을 계산한다.")
    @Test
    void findPathWithArrivalTime() {
        LocalDateTime time = LocalDateTime.of(2020, 8, 7, 11, 30);
        ExtractableResponse<Response> response = 도착시간이_가장_빠른_경로를_조회한다(1L, 3L, time);

        경로가_정상적으로_조회됨(response, 4, 3, 1250, Lists.newArrayList(1L, 2L, 3L));
    }
}
