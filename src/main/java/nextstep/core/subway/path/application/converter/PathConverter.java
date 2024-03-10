package nextstep.core.subway.path.application.converter;


import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.path.application.dto.PathRequest;

public class PathConverter {

    public static PathRequest convertToRequest(FavoriteRequest request) {
        return new PathRequest(request.getSource(), request.getTarget());
    }
}
