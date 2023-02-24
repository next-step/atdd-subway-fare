package nextstep.subway.ui;

import nextstep.member.domain.LoginUser;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("")
    public ResponseEntity<FavoriteResponse> createFavorite(@LoginUser final LoginMember loginUser, @Valid @RequestBody final FavoriteRequest favoriteRequest) {
        final FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginUser, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("{favoriteId}")
    public ResponseEntity<FavoriteResponse> showFavorite(@LoginUser final LoginMember loginUser, @PathVariable final Long favoriteId) {
        return ResponseEntity.ok(favoriteService.showFavorite(loginUser, favoriteId));
    }

    @DeleteMapping("{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@LoginUser final LoginMember loginUser, @PathVariable final Long favoriteId) {
        favoriteService.removeFavorite(loginUser, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
