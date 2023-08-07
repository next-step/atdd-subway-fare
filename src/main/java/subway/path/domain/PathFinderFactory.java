package subway.path.domain;

import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.path.domain.strategy.MinimumTimePathFinderStrategy;
import subway.path.domain.strategy.ShortestDistancePathFinderStrategy;

public class PathFinderFactory {

    public static PathFinder createFinder(PathRetrieveType type) {
        switch (type) {
            case DISTANCE:
                return new PathFinder(new ShortestDistancePathFinderStrategy());
            case DURATION:
                return new PathFinder(new MinimumTimePathFinderStrategy());
            default:
                throw new SubwayBadRequestException(SubwayMessage.PATH_INVALID_REQUEST_TYPE);
        }
    }
}
