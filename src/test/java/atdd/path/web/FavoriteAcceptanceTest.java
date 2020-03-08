package atdd.path.web;

import atdd.AbstractAcceptanceTest;
import atdd.TestConstant;
import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.application.dto.FavoriteRoutesResponseView;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.application.dto.FavoriteStationsResponseView;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.web.UserHttpTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AbstractAcceptanceTest {

    private final String FAVORITE_URI = "/favorites";

    private UserHttpTest userHttpTest;
    private FavoriteHttpTest favoriteHttpTest;
    private StationHttpTest stationHttpTest;
    private LineHttpTest lineHttpTest;

    private LoginResponseView token;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
        this.favoriteHttpTest = new FavoriteHttpTest(webTestClient);
        this.stationHttpTest = new StationHttpTest(webTestClient);
        this.lineHttpTest = new LineHttpTest(webTestClient);

        userHttpTest.createUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_NAME, TestConstant.TEST_USER_PASSWORD);
        this.token = userHttpTest.loginUser(TestConstant.TEST_USER_EMAIL, TestConstant.TEST_USER_PASSWORD).getResponseBody();
    }

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    void createFavoriteStation() {
        stationHttpTest.createStation(TestConstant.STATION_NAME);

        FavoriteStationResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("지하철역 즐겨찾기 조회")
    @Test
    void findFavoriteStation() {
        // given
        Long stationId = stationHttpTest.createStation(TestConstant.STATION_NAME);
        favoriteHttpTest.createFavoriteStation(stationId, token).getResponseBody();

        FavoriteStationsResponseView response = webTestClient.get().uri(FAVORITE_URI + "/station")
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(FavoriteStationsResponseView.class)
                .returnResult().getResponseBody();

        assertThat(response.getFavoriteStations().size()).isEqualTo(1);
        assertThat(response.getFavoriteStations().get(0).getStation().getName()).isEqualTo(TestConstant.STATION_NAME);
    }

    @DisplayName("지하철역 즐겨찾기 삭제")
    @Test
    void deleteFavoriteStation() {
        // given
        stationHttpTest.createStation(TestConstant.STATION_NAME);
        FavoriteStationResponseView response = favoriteHttpTest.createFavoriteStation(1L, token).getResponseBody();

        webTestClient.delete().uri(FAVORITE_URI + "/station/" + response.getId())
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName("경로 즐겨찾기 등록")
    @Test
    void createFavoriteRoute() {
        //given
        Long firstStationId = stationHttpTest.createStation(TestConstant.STATION_NAME);
        Long secondStationId = stationHttpTest.createStation(TestConstant.STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(TestConstant.LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, firstStationId, secondStationId);

        FavoriteRouteResponseView response = favoriteHttpTest.createFavoriteRoute(firstStationId, secondStationId, token)
                .getResponseBody();

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("경로 즐겨찾기 조회")
    @Test
    void findFavoriteRoute() {
        //given
        Long firstStationId = stationHttpTest.createStation(TestConstant.STATION_NAME);
        Long secondStationId = stationHttpTest.createStation(TestConstant.STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(TestConstant.LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, firstStationId, secondStationId);
        favoriteHttpTest.createFavoriteRoute(firstStationId, secondStationId, token);

        FavoriteRoutesResponseView response = favoriteHttpTest.findFavoriteRoute(token).getResponseBody();

        assertThat(response.getFavoriteRoutes().size()).isEqualTo(1);
        assertThat(response.getFavoriteRoutes().get(0).getSourceStation().getName()).isEqualTo(TestConstant.STATION_NAME);
        assertThat(response.getFavoriteRoutes().get(0).getTargetStation().getName()).isEqualTo(TestConstant.STATION_NAME_2);
    }

    @DisplayName("경로 즐겨찾기 삭제")
    @Test
    void deleteFavoriteRoute() {
        //given
        Long firstStationId = stationHttpTest.createStation(TestConstant.STATION_NAME);
        Long secondStationId = stationHttpTest.createStation(TestConstant.STATION_NAME_2);
        Long lineId = lineHttpTest.createLine(TestConstant.LINE_NAME);
        lineHttpTest.createEdgeRequest(lineId, firstStationId, secondStationId);
        favoriteHttpTest.createFavoriteRoute(firstStationId, secondStationId, token);

        FavoriteRoutesResponseView response = favoriteHttpTest.findFavoriteRoute(token).getResponseBody();

        webTestClient.delete().uri(FAVORITE_URI + "/route/" + response.getFavoriteRoutes().get(0).getId())
                .header("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()))
                .exchange()
                .expectStatus().isNoContent();
    }
}
