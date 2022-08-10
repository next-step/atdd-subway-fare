package nextstep.member.ui;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.userdetails.User;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal User user, @RequestBody @Valid FavoriteRequest request) {
        Long favoriteId = favoriteService.createFavorite(user.getUsername(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + favoriteId))
                .build();
    }

    @GetMapping("/favorites")
    public List<FavoriteResponse> getFavorites(@AuthenticationPrincipal User user) {
        return favoriteService.findFavorites(user.getUsername());
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal User user, @PathVariable Long id) {
        favoriteService.deleteFavorite(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
