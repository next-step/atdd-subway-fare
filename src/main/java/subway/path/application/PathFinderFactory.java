package subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.path.domain.PathRetrieveType;

@Component
@RequiredArgsConstructor
public class PathFinderFactory {

    public PathFinder createFinder(PathRetrieveType type) {
        switch (type) {
            case DISTANCE:
                return new PathFinder(new ShortestDistancePathFinder());
            case DURATION:
                return new PathFinder(new MinimumTimePathFinder());
            default:
                throw new SubwayBadRequestException(SubwayMessage.PATH_INVALID_REQUEST_TYPE);
        }
    }
}
