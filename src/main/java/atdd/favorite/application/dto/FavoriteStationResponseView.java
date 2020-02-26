package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

public class FavoriteStationResponseView {
    private Long id;
    private String userEmail;
    private Long stationId;

    public Long getFavoriteStationId() {
        return stationId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponseView() {
    }

    public FavoriteStationResponseView(Long id, String userEmail, Long stationId) {
        this.id = id;
        this.userEmail = userEmail;
        this.stationId = stationId;
    }

    public static FavoriteStationResponseView of(FavoriteStation createdFavoriteStation) {
        return new FavoriteStationResponseView(
                createdFavoriteStation.getId(),
                createdFavoriteStation.getUserEmail(),
                createdFavoriteStation.getStationId()
        );
    }

    public void insertId(Long id) {
        this.id = id;
    }
}
