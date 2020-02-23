package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.CreateStationRequestView;
import atdd.path.auth.LoginUser;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/stations")
    public ResponseEntity createStationFavorite(@RequestBody CreateStationRequestView view, @LoginUser User user) {
        return favoriteService.addStationFavorite(view, user);
    }

    @GetMapping("/stations")
    public ResponseEntity retrieveSationsFavorites(@LoginUser User user) {
        return favoriteService.retriveStationFavorites(user);
    }
}
