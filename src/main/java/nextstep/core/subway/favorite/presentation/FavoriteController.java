package nextstep.core.subway.favorite.presentation;

import nextstep.core.auth.domain.LoginUser;
import nextstep.core.auth.presentation.AuthenticationPrincipal;
import nextstep.core.member.application.MemberService;
import nextstep.core.subway.favorite.application.FavoriteService;
import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.favorite.application.dto.FavoriteResponse;
import nextstep.core.subway.favorite.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest request,
                                               @AuthenticationPrincipal LoginUser loginUser) {
        Favorite favorite = favoriteService.createFavorite(request, memberService.findMemberByEmail(loginUser.getEmail()).getId());
        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId()))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(memberService.findMemberByEmail(loginUser.getEmail())));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id,
                                               @AuthenticationPrincipal LoginUser loginUser) {
        favoriteService.deleteFavorite(id, memberService.findMemberByEmail(loginUser.getEmail()));
        return ResponseEntity.noContent().build();
    }
}
