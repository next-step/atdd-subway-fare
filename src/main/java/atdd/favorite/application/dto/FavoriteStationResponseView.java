package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;
import atdd.path.application.dto.StationResponseView;

public class FavoriteStationResponseView {

    private Long id;
    private StationResponseView station;

    private FavoriteStationResponseView() {}

    public FavoriteStationResponseView(FavoriteStation favoriteStation) {
        this.id = favoriteStation.getId();
        this.station = StationResponseView.of(favoriteStation.getStation());
    }

    public Long getId() {
        return id;
    }

    public StationResponseView getStation() {
        return station;
    }

}
