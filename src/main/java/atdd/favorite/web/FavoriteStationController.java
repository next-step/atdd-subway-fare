package atdd.favorite.web;

import atdd.favorite.application.FavoriteStationService;
import atdd.favorite.application.dto.*;
import atdd.favorite.domain.FavoriteStation;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static atdd.Constant.FAVORITE_STATION_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(FAVORITE_STATION_BASE_URI)
public class FavoriteStationController {
    private FavoriteStationService service;

    private FavoriteStationController(FavoriteStationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity createFavoriteStation(@RequestBody CreateFavoriteStationRequestView createRequestView,
                                                HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        createRequestView.insertUserEmail(email);
        FavoriteStationResponseView responseView = service.createFavoriteStation(createRequestView);
        FavoriteStationResource resource = new FavoriteStationResource(responseView);
        resource.add(linkTo(FavoriteStationController.class).withSelfRel());
        resource.add(linkTo(FavoriteStationController.class).withRel("favorite-station-showAllStations"));
        resource.add(new Link("/docs/api-guide.html#resource-find-path").withRel("profile"));
        return ResponseEntity
                .created(URI.create(FAVORITE_STATION_BASE_URI + "/" + responseView.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        FavoriteStationResponseView responseView = new FavoriteStationResponseView();
        responseView.issertId(id);
        FavoriteStationResource resource = new FavoriteStationResource(responseView);
        resource.add(linkTo(FavoriteStationController.class).slash(id).withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resource-favorite-station-delete").withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }

    @GetMapping
    public ResponseEntity showAll(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        List<FavoriteStation> favoriteStations = service.findAllByEmail(email);
        FavoriteStationsListResponseView responseView
                = new FavoriteStationsListResponseView(email, favoriteStations);
        FavoriteStationListResource resource = new FavoriteStationListResource(responseView);
        resource.add(linkTo(FavoriteStationController.class).withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resource-favorite-station-showAllFavoriteStations").withRel("profile"));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}
