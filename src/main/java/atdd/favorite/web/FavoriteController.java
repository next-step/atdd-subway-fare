package atdd.favorite.web;

import atdd.favorite.application.FavoriteService;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.application.dto.FavoritePathsResponseView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.application.dto.FavoriteStationsResponseView;
import atdd.favorite.domain.FavoritePath;
import atdd.favorite.domain.FavoriteStation;
import atdd.member.domain.Member;
import atdd.member.web.LoginUser;
import atdd.path.application.GraphService;
import atdd.path.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    public static final String FAVORITES_STATIONS_URL = "/favorites/stations";
    public static final String FAVORITES_PATH_URL = "/favorites/paths";

    private final FavoriteService favoriteService;
    private final GraphService graphService;

    public FavoriteController(FavoriteService favoriteService, GraphService graphService) {
        this.favoriteService = favoriteService;
        this.graphService = graphService;
    }

    @PostMapping("/stations/{id}")
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@PathVariable("id") Long stationId,
                                                                             @LoginUser Member member) {

        final FavoriteStation savedFavoriteStation = favoriteService.saveForStation(member, stationId);

        return ResponseEntity.created(URI.create(FAVORITES_STATIONS_URL +"/"+ savedFavoriteStation.getId()))
                .body(new FavoriteStationResponseView(savedFavoriteStation));
    }

    @GetMapping("/stations")
    public ResponseEntity<FavoriteStationsResponseView> findFavoriteStations(@LoginUser Member member) {
        final List<FavoriteStation> favorites = favoriteService.findForStations(member);
        return ResponseEntity.ok(new FavoriteStationsResponseView(favorites));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Object> deleteFavoriteStation(@PathVariable("id") Long favoriteId) {
        favoriteService.deleteForStationById(favoriteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/paths")
    public ResponseEntity<FavoritePathResponseView> createFavoritePath(@RequestParam Long startId,
                                                                       @RequestParam Long endId,
                                                                       @LoginUser Member member) {

        final FavoritePath savedFavoritePath = favoriteService.saveForPath(member, startId, endId);
        final List<Station> stations = graphService.findPath(savedFavoritePath);

        return ResponseEntity.created(URI.create(FAVORITES_PATH_URL +"/"+ savedFavoritePath.getId()))
                .body(new FavoritePathResponseView(savedFavoritePath, stations));
    }

    @GetMapping("/paths")
    public ResponseEntity<FavoritePathsResponseView> findFavoritePath(@LoginUser Member member) {
        final List<FavoritePath> findFavoritePaths = favoriteService.findForPaths(member);

        final List<FavoritePathResponseView> views = findFavoritePaths.stream()
                .map(path -> new FavoritePathResponseView(path, graphService.findPath(path)))
                .collect(toList());

        return ResponseEntity.ok(new FavoritePathsResponseView(views));
    }

    @DeleteMapping("/paths/{id}")
    public ResponseEntity<Object> deleteFavoritePaths(@PathVariable("id") Long favoriteId) {
        favoriteService.deleteForPathById(favoriteId);
        return ResponseEntity.noContent().build();
    }

}