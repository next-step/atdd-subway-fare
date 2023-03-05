package nextstep.subway.ui;

import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.AuthMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal AuthMember authMember, @RequestBody FavoriteRequest request) {
        favoriteService.createFavorite(authMember.getId(), request);
        return ResponseEntity
                .created(URI.create("/favorites/" + 1L))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal AuthMember authMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(authMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal AuthMember authMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(authMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
