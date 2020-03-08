package atdd.path.application.dto;

import atdd.path.domain.FavoriteRoute;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FavoriteRoutesResponseView {
    private List<FavoriteRouteResponseView> favoriteRoutes;

    @Builder
    public FavoriteRoutesResponseView(List<FavoriteRouteResponseView> favoriteRoutes) {
        this.favoriteRoutes = favoriteRoutes;
    }

    public static FavoriteRoutesResponseView of(List<FavoriteRoute> favorites) {
        return FavoriteRoutesResponseView.builder()
                .favoriteRoutes(favorites.stream()
                        .map(it -> FavoriteRouteResponseView.of(it))
                        .collect(Collectors.toList()))
                .build();
    }
}
