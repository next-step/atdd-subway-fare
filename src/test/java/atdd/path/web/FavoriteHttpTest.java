package atdd.path.web;

import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.application.dto.FavoriteRoutesResponseView;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.user.application.dto.LoginResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class FavoriteHttpTest {
    public static final String FAVORITE_URL = "/favorites";
    private final WebTestClient webTestClient;

    public FavoriteHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<FavoriteStationResponseView> createFavoriteStation(Long stationId, LoginResponseView token) {
        String request = "{\"stationId\": " + stationId + "}";

        return webTestClient.post().uri(FAVORITE_URL + "/station")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .body(Mono.just(request), String.class)
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(FavoriteStationResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<FavoriteRouteResponseView> createFavoriteRoute(Long sourceStationId, Long targetStationId, LoginResponseView token) {
        String request = "{" +
                "\"sourceStationId\":" + sourceStationId + ", " +
                "\"targetStationId\":" + targetStationId + "}";

        return webTestClient.post().uri(FAVORITE_URL + "/route")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), String.class)
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectHeader().exists("Location")
                .expectStatus().isCreated()
                .expectBody(FavoriteRouteResponseView.class)
                .returnResult();
    }

    public EntityExchangeResult<FavoriteRoutesResponseView> findFavoriteRoute(LoginResponseView token) {
        return webTestClient.get().uri(FAVORITE_URL + "/route")
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteRoutesResponseView.class)
                .returnResult();
    }
}
