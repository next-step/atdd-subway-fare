package atdd.path.web;

import atdd.path.application.dto.LineResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class LineHttpTest {
    public static final String LINE_URL = "/lines";
    public WebTestClient webTestClient;

    public LineHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<LineResponseView> createLineRequest(String name,
                                                                    String startTime,
                                                                    String endTime,
                                                                    int interval) {
        String inputJson = "{\"name\":\"" + name + "\"," +
                "\"startTime\":\"" + startTime + "\"," +
                "\"endTime\":\"" + endTime + "\"," +
                "\"interval\":\"" + interval + "\"}";

        return webTestClient.post().uri(LINE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(LineResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<LineResponseView> retrieveLineRequest(String uri) {
        return webTestClient.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<List<LineResponseView>> showLinesRequest() {
        return webTestClient.get().uri(LINE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(LineResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<LineResponseView> retrieveLine(Long lineId) {
        return retrieveLineRequest(LINE_URL + "/" + lineId);
    }

    public EntityExchangeResult createEdgeRequest(Long lineId, Long stationId, Long stationId2, int elapsedTime, int distance) {
        String inputJson = "{\"sourceId\":" + stationId +
                ",\"targetId\":" + stationId2 +
                ",\"elapsedTime\":" + elapsedTime +
                ",\"distance\":" + distance + "}";

        return webTestClient.post().uri("/lines/" + lineId + "/edges")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();
    }
}
