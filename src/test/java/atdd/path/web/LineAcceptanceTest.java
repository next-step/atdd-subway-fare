package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.path.application.dto.LineResponseView;
import atdd.path.application.dto.StationResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalTime;
import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineAcceptanceTest extends AbstractAcceptanceTest {
    public static final String LINE_URL = "/lines";
    public static final String EDGE_URL = "/edges";

    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    private LineResponseView lineResponseView;

    private StationResponseView stationResponseView;
    private StationResponseView stationResponseView2;
    private StationResponseView stationResponseView3;

    @BeforeEach
    void setUp() {
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);

        this.lineResponseView = lineHttpTest.createLineRequest(LINE_NAME, "05:45", "00:05", 10).getResponseBody();
        this.stationResponseView = stationHttpTest.createStationRequest(STATION_NAME).getResponseBody();
        this.stationResponseView2 = stationHttpTest.createStationRequest(STATION_NAME_2).getResponseBody();
        this.stationResponseView3 = stationHttpTest.createStationRequest(STATION_NAME_3).getResponseBody();
    }

    @DisplayName("지하철 노선 등록")
    @Test
    public void createLine() {
        // then
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineResponseView.getId());
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
    }

    @DisplayName("지하철 노선 정보 조회")
    @Test
    public void retrieveLine() {
        // when
        EntityExchangeResult<LineResponseView> getResponse = lineHttpTest.retrieveLine(lineResponseView.getId());

        // then
        assertThat(getResponse.getResponseBody().getName()).isEqualTo(LINE_NAME);
        assertThat(getResponse.getResponseBody().getStartTime()).isEqualTo(LocalTime.of(0, 0).toString());
        assertThat(getResponse.getResponseBody().getEndTime()).isEqualTo(LocalTime.of(23, 30).toString());
        assertThat(getResponse.getResponseBody().getInterval()).isEqualTo(30);
    }

    @DisplayName("구건이 연결된 지하철 노선 조회")
    @Test
    public void retrieveLineWithStation() {
        // given
        long lineId = lineResponseView.getId();

        Long stationId = stationResponseView.getId();
        Long stationId2 = stationResponseView2.getId();
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, 5, 10);

        // when
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);

        // then
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    public void showLines() {
        // given
        lineHttpTest.createLineRequest(LINE_NAME_2, "05:45", "00:05", 10);
        lineHttpTest.createLineRequest(LINE_NAME_3, "05:45", "00:05", 10);

        // when
        EntityExchangeResult<List<LineResponseView>> response = lineHttpTest.showLinesRequest();

        // then
        assertThat(response.getResponseBody().size()).isEqualTo(3);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    public void deleteLine() {
        //given
        long lineId = lineResponseView.getId();
        // when
        webTestClient.delete().uri(LINE_URL + "/" + lineId)
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(LINE_URL + "/" + lineId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("지하철노선에 지하철 구간을 등록")
    @Test
    public void createEdge() {
        // given
        long lineId = lineResponseView.getId();

        Long stationId = stationResponseView.getId();
        Long stationId2 = stationResponseView2.getId();

        // when
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, 5, 10);

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_2);
    }

    @DisplayName("지하철노선에 지하철 구간을 제외")
    @Test
    public void deleteEdge() {
        // given
        long lineId = lineResponseView.getId();

        Long stationId = stationResponseView.getId();
        Long stationId2 = stationResponseView2.getId();
        Long stationId3 = stationResponseView3.getId();

        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3, 5, 10);

        // when
        webTestClient.delete().uri(LINE_URL + "/" + lineId + EDGE_URL + "?stationId=" + stationId2)
                .exchange()
                .expectStatus().isOk();

        // then
        EntityExchangeResult<LineResponseView> lineResult = lineHttpTest.retrieveLineRequest(LINE_URL + "/" + lineId);
        assertThat(lineResult.getResponseBody().getStations().size()).isEqualTo(2);
        assertThat(lineResult.getResponseBody().getStations().get(0).getName()).isEqualTo(STATION_NAME);
        assertThat(lineResult.getResponseBody().getStations().get(1).getName()).isEqualTo(STATION_NAME_3);
    }
}
