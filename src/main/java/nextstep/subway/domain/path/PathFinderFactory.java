package nextstep.subway.domain.path;

import nextstep.subway.domain.path.fee.AdditionalFeeHandler;
import nextstep.subway.domain.path.fee.DistanceCalculateHandler;

public class PathFinderFactory {

    public static PathFinder create(PathType pathType) {
        return pathType.createPathFinder(new DistanceCalculateHandler(new AdditionalFeeHandler(null)));
    }
}
