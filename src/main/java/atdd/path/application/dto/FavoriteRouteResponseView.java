package atdd.path.application.dto;

import atdd.path.domain.FavoriteRoute;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRouteResponseView {
    private Long id;
    private ItemView sourceStation;
    private ItemView targetStation;

    @Builder
    public FavoriteRouteResponseView(Long id, ItemView sourceStation, ItemView targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static FavoriteRouteResponseView of(FavoriteRoute favorite) {
        return FavoriteRouteResponseView.builder()
                .id(favorite.getId())
                .sourceStation(ItemView.builder()
                        .id(favorite.getSourceStation().getId())
                        .name(favorite.getSourceStation().getName())
                        .build())
                .targetStation(ItemView.builder()
                        .id(favorite.getTargetStation().getId())
                        .name(favorite.getTargetStation().getName())
                        .build())
                .build();
    }
}
