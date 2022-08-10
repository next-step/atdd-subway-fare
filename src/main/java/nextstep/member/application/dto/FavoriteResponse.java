package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.station.application.dto.StationResponse;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public static FavoriteResponse of(Favorite favorite, StationResponse source, StationResponse target) {
        return new FavoriteResponse(favorite.getId(), source, target);
    }

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
