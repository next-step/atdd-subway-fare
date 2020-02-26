package atdd.favorite.web;

import atdd.favorite.application.FavoritePathService;
import atdd.favorite.application.dto.*;
import atdd.favorite.domain.FavoritePath;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static atdd.Constant.FAVORITE_PATH_BASE_URI;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(FAVORITE_PATH_BASE_URI)
public class FavoritePathController {
    private FavoritePathService service;

    private FavoritePathController(FavoritePathService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity createFavoritePath(@RequestBody CreateFavoritePathRequestView request,
                                             HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        request.insertUserEmail(email);
        FavoritePathResponseView response = service.create(request);

        FavoritePathResource resource = new FavoritePathResource(response);
        resource.add(linkTo(FavoritePathController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resource-favorite-path-create")
                .withRel("profile"));

        return ResponseEntity
                .created(URI.create(FAVORITE_PATH_BASE_URI + "/" + response.getId()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavoritePath(@PathVariable Long id) {
        service.delete(id);
        FavoritePathResponseView responseView = new FavoritePathResponseView(id);

        FavoritePathResource resource = new FavoritePathResource(responseView);
        resource.add(linkTo(FavoritePathController.class)
                .slash(id)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resource-favorite-path-create")
                .withRel("profile"));

        return ResponseEntity
                .ok(resource);
    }

    @GetMapping
    public ResponseEntity showFavoritePaths(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        List<FavoritePath> favoritePaths = service.findAllByEmail(email);
        FavoritePathListResponseView responseView
                = new FavoritePathListResponseView(email, favoritePaths);

        FavoritePathListResource resource = new FavoritePathListResource(responseView);
        resource.add(linkTo(FavoritePathController.class)
                .withSelfRel());
        resource.add(new Link("/docs/api-guide.html#resource-favorite-path-showAllFavoritePaths")
                .withRel("profile"));

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }
}
