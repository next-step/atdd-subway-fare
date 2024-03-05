package nextstep.core.subway.pathFinder.application.converter;


import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;

public class PathFinderConverter {

    public static PathFinderRequest convertToRequest(FavoriteRequest request) {
        return new PathFinderRequest(request.getSource(), request.getTarget());
    }
}
