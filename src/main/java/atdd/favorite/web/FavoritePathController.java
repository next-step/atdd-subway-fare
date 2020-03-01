package atdd.favorite.web;

import atdd.favorite.application.dto.*;
import atdd.favorite.service.FavoritePathService;
import atdd.user.domain.User;
import org.springframework.http.MediaType;
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
        return ResponseEntity
                .created(URI.create(FAVORITE_PATH_BASE_URI + "/" + responseView.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@LoginUser User user,
                                 @PathVariable Long id) throws Exception {
        FavoritePathRequestView requestView = new FavoritePathRequestView();
        requestView.insertEmail(user.getEmail());
        requestView.insertId(id);
        favoritePathService.delete(requestView);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping
    public ResponseEntity showAll(@LoginUser User user){
        FavoritePathRequestView requestView = new FavoritePathRequestView();
        requestView.insertEmail(user.getEmail());
        FavoritePathListResponseView responseView
                = favoritePathService.showAllFavoritePath(requestView);
        return ResponseEntity
                .ok()
                .body(responseView.getFavoritePaths());
    }
}
