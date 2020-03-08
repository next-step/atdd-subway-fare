package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FavoriteStationResponseView {
    private Long id;
    private ItemView station;

    @Builder
    public FavoriteStationResponseView(Long id, Station station) {
        this.id = id;
        this.station = ItemView.builder().id(station.getId()).name(station.getName()).build();
    }

    public static FavoriteStationResponseView of(FavoriteStation savedFavorite) {
        return FavoriteStationResponseView.builder()
                .id(savedFavorite.getId())
                .station(savedFavorite.getStation())
                .build();
    }
}
