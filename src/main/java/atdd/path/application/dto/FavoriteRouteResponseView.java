package atdd.path.application.dto;

import atdd.path.domain.FavoriteRoute;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FavoriteRouteResponseView {
    private Long id;
    private ItemView sourceStation;
    private ItemView targetStation;

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

    public static List<FavoriteRouteResponseView> listOf(List<FavoriteRoute> favorites) {
        return favorites.stream()
                .map(it -> FavoriteRouteResponseView.builder()
                        .id(it.getId())
                        .sourceStation(ItemView.builder()
                                .id(it.getSourceStation().getId())
                                .name(it.getSourceStation().getName())
                                .build())
                        .targetStation(ItemView.builder()
                                .id(it.getTargetStation().getId())
                                .name(it.getTargetStation().getName())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
