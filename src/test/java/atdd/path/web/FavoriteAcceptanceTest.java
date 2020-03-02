package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {
    public static final String FAVORITE_URI = "/favorites";
    private String tokenInfo;

    private UserHttpTest userHttpTest;
    private StationHttpTest stationHttpTest;

    @BeforeEach()
    void setUp() {
        userHttpTest = new UserHttpTest(webTestClient);
        stationHttpTest = new StationHttpTest(webTestClient);

        String userUri = "/users";
        String userInputJson = String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\"}",
                "boorwonie@email.com", "브라운", "subway");
        String signInUri = userUri + "/login";
        String signInJson = String.format("{\"email\": \"%s\", \"password\" : \"%s\"}", "boorwonie@email.com",
                "subway");

        userHttpTest.createUserSuccess(userUri, userInputJson);
        tokenInfo = userHttpTest.createAuthorizationTokenSuccess(signInUri, signInJson);

        stationHttpTest.createStation("강남역");
    }

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    public void registerFavoriteForStation() {
        String inputJson = String.format("{\"name\": \"%s\"}", "강남역");

        webTestClient
                .post()
                .uri(FAVORITE_URI + "/stations")
                .header("Authorization", tokenInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(FavoriteResponseView.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                });
    }

    @DisplayName("지하철역 즐겨찾기 조회")
    @Test
    public void retrieveFavoriteForStations() {
        String inputJson = String.format("{\"name\": \"%s\"}", "강남역");

        webTestClient
                .post()
                .uri(FAVORITE_URI + "/stations")
                .header("Authorization", tokenInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(FavoriteResponseView.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotNull();
                });

        webTestClient.get().uri(FAVORITE_URI + "/stations").header("Authorization", tokenInfo)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteResponseView.class)
                .consumeWith(result -> {
                    List<Station> stationList = result.getResponseBody().getStations();
                    Station favoritedStation = stationList.stream().filter(station -> station.getName().equals("강남역")).findFirst().orElse(null);
                    assertThat(favoritedStation).isNotNull();
                });
    }
}
