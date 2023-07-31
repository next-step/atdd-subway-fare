package nextstep.favorite.service.dto;

import lombok.Data;
import nextstep.subway.domain.service.StationPathSearchRequestType;

@Data
public class FavoritePathRequest {
    private Long source;
    private Long target;
    private StationPathSearchRequestType type;
}
