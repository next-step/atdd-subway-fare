package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.application.dto.StationResponseView;
import atdd.path.application.dto.TimeTableResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AbstractAcceptanceTest {
    public static final String STATION_URL = "/stations";
    private LineHttpTest lineHttpTest;
    private StationHttpTest stationHttpTest;

    @BeforeEach
    void setUp() {
        this.lineHttpTest = new LineHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
    }

    @Test
    public void 지하철역_등록_요청하기() {
        // when
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // then
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @Test
    public void 지하철역_정보_조회_요청하기() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @Test
    public void 구간이_연결된_지하철역_정보_조회_요청하기() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long lineId = lineHttpTest.createLine(LINE_NAME);
        Long lineId2 = lineHttpTest.createLine(LINE_NAME_2);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        lineHttpTest.createEdgeRequest(lineId2, stationId, stationId3);

        // when
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // then
        assertThat(response.getResponseBody().getId()).isEqualTo(stationId);
        assertThat(response.getResponseBody().getName()).isEqualTo(STATION_NAME);
    }

    @Test
    public void 지하철역_목록_조회_요청하기() {
        // given
        stationHttpTest.createStationRequest(STATION_NAME);
        stationHttpTest.createStationRequest(STATION_NAME_2);
        stationHttpTest.createStationRequest(STATION_NAME_3);

        // when
        EntityExchangeResult<List<StationResponseView>> response = stationHttpTest.showStationsRequest();

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @Test
    public void 지하철역_삭제_요청하기() {
        // given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        EntityExchangeResult<StationResponseView> response = stationHttpTest.retrieveStation(stationId);

        // when
        webTestClient.delete().uri(STATION_URL + "/" + stationId)
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(STATION_URL + "/" + stationId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void 지하철역_시간표_조회_요청하기() {
        //given
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_6);
        Long lineId = lineHttpTest.createLine(LINE_NAME_2);
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2);
        int theNumberOfLinesForStation = 1;

        //when
        List<TimeTableResponseView> responseView
                = stationHttpTest.showTimeTablesForUpAndDown(STATION_NAME, stationId);

        //then
        assertThat(responseView.size()).isEqualTo(theNumberOfLinesForStation);
        assertThat(responseView.get(0).getLineId()).isEqualTo(lineId);
        assertThat(responseView.get(0).getLineName()).isEqualTo(LINE_NAME_2);
        assertThat(responseView.get(0).getTimeTables()).isNotNull();
    }
}
