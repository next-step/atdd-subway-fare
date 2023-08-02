package subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.path.domain.PathRetrieveType;

@Component
@RequiredArgsConstructor
public class PathFinderFactory {
    private final MinimumTimePathFinder minimumTimePathStrategy;
    private final ShortestDistancePathFinder shortestDistancePathStrategy;

    public PathFinder createFinder(PathRetrieveType type) {
        switch (type) {
            case DISTANCE:
                return new PathFinder(shortestDistancePathStrategy);
            case DURATION:
                return new PathFinder(minimumTimePathStrategy);
            default:
                throw new SubwayBadRequestException(SubwayMessage.PATH_INVALID_REQUEST_TYPE);
        }
    }
}
