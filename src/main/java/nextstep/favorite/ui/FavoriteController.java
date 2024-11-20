package nextstep.favorite.ui;

import nextstep.auth.domain.Account;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.auth.ui.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal Account account) {
        FavoriteResponse response = favoriteService.createFavorite(favoriteRequest, account);
        return ResponseEntity
                .created(URI.create("/favorites/" + response.getId()))
                .body(response);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal Account account) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(account);
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        favoriteService.deleteFavorite(id, account);
        return ResponseEntity.noContent().build();
    }
}
