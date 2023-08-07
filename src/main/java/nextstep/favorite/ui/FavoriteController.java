package nextstep.favorite.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.userdetails.UserDetails;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal UserDetails userDetails, @RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(userDetails.getUsername(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(userDetails.getUsername());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        favoriteService.deleteFavorite(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
