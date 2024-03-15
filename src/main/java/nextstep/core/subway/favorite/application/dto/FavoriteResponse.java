package nextstep.core.subway.favorite.application.dto;

import nextstep.core.subway.favorite.domain.Favorite;
import nextstep.core.subway.station.application.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResponse {

    public Long id;
    public StationResponse source;
    public StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static List<FavoriteResponse> toResponse(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();

        favorites.forEach(favorite -> {
            favoriteResponses.add(new FavoriteResponse(
                    favorite.getId(),
                    new StationResponse(favorite.getSourceStation().getId(), favorite.getSourceStation().getName()),
                    new StationResponse(favorite.getTargetStation().getId(), favorite.getTargetStation().getName())));
        });
        return favoriteResponses;
    }
}
