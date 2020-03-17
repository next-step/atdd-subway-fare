package atdd.path.web;

import atdd.path.application.dto.MinTimePathResponseView;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class PathHttpTest {
    private static final String PATH_URL = "/paths";

    private WebTestClient webTestClient;

    public PathHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<MinTimePathResponseView> findMinTimePathRequest(long startId, long endId) {
        return webTestClient.get().uri(PATH_URL + "/min-time?" + "startId=" + startId + "&endId=" + endId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MinTimePathResponseView.class)
                .returnResult();
    }
}