package nextstep.subway.ui;

import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.service.FavoriteService;
import org.springframework.http.HttpStatus;
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
    ResponseEntity createFavorite(@RequestBody FavoriteRequest request, @AuthenticationPrincipal Long memberId) {
        Long favoriteId = favoriteService.createFavorite(request, memberId);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal Long memberId) {
        List<FavoriteResponse> responseList = favoriteService.getFavorites(memberId);
        return ResponseEntity.ok()
                .body(responseList);
    }

    @DeleteMapping("/favorites/{id}")
    ResponseEntity deleteFavorite(@PathVariable long id, @AuthenticationPrincipal Long memberId) {
        favoriteService.deleteFavorite(id, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
