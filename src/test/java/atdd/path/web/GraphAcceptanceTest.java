package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static atdd.TestConstant.*;

public class GraphAcceptanceTest extends AbstractAcceptanceTest {
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    @BeforeEach
    void setUp() {
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);
    }

    @Test
    public void findPath() {
        Long stationId = stationHttpTest.createStation(STATION_NAME);
        Long stationId2 = stationHttpTest.createStation(STATION_NAME_2);
        Long stationId3 = stationHttpTest.createStation(STATION_NAME_3);
        Long stationId4 = stationHttpTest.createStation(STATION_NAME_4);
        Long lineId = lineHttpTest.createLineRequest(LINE_NAME, "05:45", "00:05", 10).getResponseBody().getId();
        lineHttpTest.createEdgeRequest(lineId, stationId, stationId2, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId2, stationId3, 5, 10);
        lineHttpTest.createEdgeRequest(lineId, stationId3, stationId4, 5, 10);

        webTestClient.get().uri("/paths?startId=" + stationId + "&endId=" + stationId4)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.startStationId").isEqualTo(stationId)
                .jsonPath("$.endStationId").isEqualTo(stationId4)
                .jsonPath("$.stations.length()").isEqualTo(4);
    }

}
