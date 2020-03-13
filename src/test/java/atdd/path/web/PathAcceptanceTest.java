package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.application.dto.MinTimePathResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AbstractAcceptanceTest {
    private LineHttpTest lineHttpTest;
    private StationHttpTest stationHttpTest;
    private PathHttpTest pathHttpTest;

    @BeforeEach
    void setUp() {
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.pathHttpTest = new PathHttpTest(webTestClient);
    }

    @DisplayName("최단 시간 경로 조회")
    @Test
    public void findMinTimePath() {
        //given
        Long stationId = stationHttpTest.createStation(STATION_NAME); // 강남역
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long stationId5 = stationHttpTest.createStation(STATION_NAME_5);

        Long stationId11 = stationHttpTest.createStation(STATION_NAME_11);
        Long stationId12 = stationHttpTest.createStation(STATION_NAME_12); // 교대역
        Long stationId13 = stationHttpTest.createStation(STATION_NAME_13);
        Long stationId14 = stationHttpTest.createStation(STATION_NAME_14);

        Long lineId = lineHttpTest.createLineRequest(LINE_NAME, "05:45", "00:05", 10).getResponseBody().getId();
        Long lineId3 = lineHttpTest.createLineRequest(LINE_NAME_3, "05:45", "00:05", 10).getResponseBody().getId();

        lineHttpTest.createEdgeRequest(lineId, stationId12, stationId, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId4, stationId5, 5, 10);

        lineHttpTest.createEdgeRequest(lineId3, stationId11, stationId12, 5, 10);
        lineHttpTest.createEdgeRequest(lineId3, stationId12, stationId13, 5, 10);
        lineHttpTest.createEdgeRequest(lineId3, stationId13, stationId14, 5, 10);

        //when
        MinTimePathResponseView minTimePathResponseView = pathHttpTest.findMinTimePathRequest(stationId11, stationId4).getResponseBody();

        //then
        assertThat(minTimePathResponseView.getStartStationId()).isEqualTo(stationId11);
        assertThat(minTimePathResponseView.getEndStationId()).isEqualTo(stationId4);
        assertThat(minTimePathResponseView.getStations().size()).isEqualTo(6);
        assertThat(minTimePathResponseView.getLines().size()).isEqualTo(2);
    }
}
