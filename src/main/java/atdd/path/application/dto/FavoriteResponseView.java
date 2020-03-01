package atdd.path.application.dto;

import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import java.util.List;

public class FavoriteResponseView {
    private Long id;
    private List<Station> stations;

    public FavoriteResponseView() {
    }

    public FavoriteResponseView(Long id, List<Station> stations) {
        this.id = id;
        this.stations = stations;
    }


    public static FavoriteResponseView of(Favorite favorite) {
        return new FavoriteResponseView(favorite.getId(), favorite.getStations());
    }


    public Long getId() {
        return id;
    }

    public List<Station> getStations() {
        return stations;
    }
}
