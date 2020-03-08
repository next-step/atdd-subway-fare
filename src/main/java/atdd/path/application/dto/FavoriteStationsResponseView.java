package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class FavoriteStationsResponseView {
    private List<FavoriteStationResponseView> favoriteStations;

    @Builder
    public FavoriteStationsResponseView(List<FavoriteStationResponseView> favoriteStations) {
        this.favoriteStations = favoriteStations;
    }

    public static FavoriteStationsResponseView of(List<FavoriteStation> favorites) {
        return FavoriteStationsResponseView.builder()
                .favoriteStations(favorites.stream()
                        .map(it -> FavoriteStationResponseView.of(it))
                        .collect(Collectors.toList()))
                .build();
    }
}
