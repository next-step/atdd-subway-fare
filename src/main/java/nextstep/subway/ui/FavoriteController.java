package nextstep.subway.ui;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import support.auth.authorization.AuthenticationPrincipal;
import support.auth.userdetails.User;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(user.getUsername(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal User user) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(user.getUsername());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal User user, @PathVariable Long id) {
        favoriteService.deleteFavorite(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
