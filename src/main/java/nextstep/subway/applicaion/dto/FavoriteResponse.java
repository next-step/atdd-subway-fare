package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Favorite;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite, StationResponse source, StationResponse target) {
        return new FavoriteResponse(favorite.getId(), source, target);
    }
}
