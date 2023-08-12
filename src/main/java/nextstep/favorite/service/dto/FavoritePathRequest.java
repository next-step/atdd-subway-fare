package nextstep.favorite.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nextstep.subway.domain.service.path.StationPathSearchRequestType;

@Data
@AllArgsConstructor
public class FavoritePathRequest {
    private Long source;
    private Long target;
    private StationPathSearchRequestType type;
}
