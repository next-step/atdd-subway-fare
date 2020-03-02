package atdd.favorite.web;

import atdd.favorite.application.dto.*;
import atdd.favorite.service.FavoriteStationService;
import atdd.user.domain.User;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static atdd.favorite.FavoriteConstant.FAVORITE_STATION_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(FAVORITE_STATION_BASE_URI)
public class FavoriteStationController {
    private FavoriteStationService favoriteStationService;

    public FavoriteStationController(FavoriteStationService favoriteStationService) {
        this.favoriteStationService = favoriteStationService;
    }

    @PostMapping
    public ResponseEntity create(@LoginUser User user,
                                 @RequestBody FavoriteStationRequestView requestView) {
        requestView.insertEmail(user.getEmail());
        FavoriteStationResponseView responseView = favoriteStationService.create(requestView);
        FavoriteStationResponseResource resource
                = new FavoriteStationResponseResource(responseView);
        resource.add(linkTo(FavoriteStationController.class)
                        .withSelfRel());
        resource.add(linkTo(FavoriteStationController.class)
                        .withRel("favorite-station-showAll"));
        resource.add(linkTo(FavoriteStationController.class)
                        .slash(responseView.getId())
                        .withRel("favorite-station-delete"));
        resource.add(new Link("/docs/api-guide.html#resources-favorite-station-create")
                .withRel("profile"));
        return ResponseEntity
                .created(URI.create(FAVORITE_STATION_BASE_URI + "/" + responseView.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@LoginUser User user,
                                 @PathVariable Long id) throws Exception {
        FavoriteStationRequestView requestView = new FavoriteStationRequestView();
        requestView.insertEmail(user.getEmail());
        requestView.insertId(id);
        favoriteStationService.delete(requestView);
        FavoriteStationResponseView responseView = new FavoriteStationResponseView();
        FavoriteStationResponseResource resource = new FavoriteStationResponseResource(responseView);
        resource.add(linkTo(FavoriteStationController.class)
                .slash(id)
                .withSelfRel());
        resource.add(linkTo(FavoriteStationController.class)
                .withRel("favorite-station-showAll"));
        resource.add(new Link("/docs/api-guide.html#resources-favorite-station-delete")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }

    @GetMapping
    public ResponseEntity showAll(@LoginUser User user) {
        FavoriteStationRequestView requestView = new FavoriteStationRequestView();
        requestView.insertEmail(user.getEmail());
        FavoriteStationListResponseVIew responseVIew
                = favoriteStationService.showAllFavoriteStations(requestView);
        FavoriteStationListResponseResource resource
                = new FavoriteStationListResponseResource(responseVIew);
        resource.add(linkTo(FavoriteStationController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-favorite-station-showAll")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }
}
