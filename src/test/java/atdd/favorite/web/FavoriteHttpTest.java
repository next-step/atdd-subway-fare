package atdd.favorite.web;

import atdd.AbstractHttpTest;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.application.dto.FavoritePathsResponseView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.application.dto.FavoriteStationsResponseView;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static atdd.TestUtils.jsonOf;
import static atdd.favorite.web.FavoriteController.FAVORITES_PATH_URL;
import static atdd.favorite.web.FavoriteController.FAVORITES_STATIONS_URL;


public class FavoriteHttpTest extends AbstractHttpTest {

    public FavoriteHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    public FavoriteStationResponseView createForStation(Long stationId, String token) {
        EntityExchangeResult<FavoriteStationResponseView> result = createForStationRequest(stationId, token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationResponseView> createForStationRequest(Long stationId, String token) {
        String inputJson = jsonOf(Map.of("stationId", stationId));
        return createRequestWithToken(FavoriteStationResponseView.class, FAVORITES_STATIONS_URL, token, inputJson);
    }

    public FavoriteStationsResponseView findForStations(String token) {
        EntityExchangeResult<FavoriteStationsResponseView> result = findForStationRequest(token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoriteStationsResponseView> findForStationRequest(String token) {
        return findRequestWithToken(FavoriteStationsResponseView.class, FAVORITES_STATIONS_URL, token);
    }

    public FavoritePathResponseView createForPath(Long startId, Long endId, String token) {
        EntityExchangeResult<FavoritePathResponseView> result = createForPathRequest(startId, endId, token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoritePathResponseView> createForPathRequest(Long startId, Long endId, String token) {
        String inputJson = jsonOf(Map.of("startId", startId, "endId", endId));
        return createRequestWithToken(FavoritePathResponseView.class,
                FAVORITES_PATH_URL, token, inputJson);
    }

    public FavoritePathsResponseView findForPaths(String token) {
        EntityExchangeResult<FavoritePathsResponseView> result = findForPathsRequest(token);
        return result.getResponseBody();
    }

    public EntityExchangeResult<FavoritePathsResponseView> findForPathsRequest(String token) {
        return findRequestWithToken(FavoritePathsResponseView.class, FAVORITES_PATH_URL, token);
    }

}
