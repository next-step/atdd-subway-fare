package atdd.favorite.web;

import atdd.favorite.application.dto.*;
import atdd.favorite.service.FavoritePathService;
import atdd.user.domain.User;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static atdd.favorite.FavoriteConstant.FAVORITE_PATH_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(FAVORITE_PATH_BASE_URI)
public class FavoritePathController {
    private FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity create(@LoginUser User user,
                                 @RequestBody FavoritePathRequestView requestView) throws Exception {
        requestView.insertEmail(user.getEmail());
        FavoritePathResponseView responseView = favoritePathService.create(requestView);
        FavoritePathResponseResource resource = new FavoritePathResponseResource(responseView);
        resource.add(linkTo(FavoritePathController.class)
                .withSelfRel());
        resource.add(linkTo(FavoritePathController.class)
                .withRel("favorite-path-showAll"));
        resource.add(linkTo(FavoritePathController.class)
                .slash(responseView.getId())
                .withRel("favorite-path-delete"));
        resource.add(new Link("/docs/api-guide.html#resources-favorite-path-create")
                .withRel("profile"));
        return ResponseEntity
                .created(URI.create(FAVORITE_PATH_BASE_URI + "/" + responseView.getId()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@LoginUser User user,
                                 @PathVariable Long id) throws Exception {
        FavoritePathRequestView requestView = new FavoritePathRequestView();
        requestView.insertEmail(user.getEmail());
        requestView.insertId(id);
        favoritePathService.delete(requestView);
        FavoritePathResponseResource resource
                = new FavoritePathResponseResource(new FavoritePathResponseView());
        resource.add(linkTo(FavoritePathController.class)
                .slash(id)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-favorite-path-delete")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }

    @GetMapping
    public ResponseEntity showAll(@LoginUser User user) {
        FavoritePathRequestView requestView = new FavoritePathRequestView();
        requestView.insertEmail(user.getEmail());
        FavoritePathListResponseView responseView
                = favoritePathService.showAllFavoritePath(requestView);
        FavoritePathListResponseResource resource
                = new FavoritePathListResponseResource(responseView);
        resource.add(linkTo(FavoritePathController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resources-favorite-path-showAll")
                .withRel("profile"));
        return ResponseEntity
                .ok(resource);
    }
}
