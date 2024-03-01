package nextstep.subway.domain.path;

public class PathFinderFactory {

    public static PathFinder create(PathType pathType) {
        if (PathType.DISTANCE.equals(pathType)) {
            return new ShortestDistancePathFinder(new DistanceCalculateHandler(null));
        } else if (PathType.DURATION.equals(pathType)) {
            return new ShortestDurationPathFinder(new DistanceCalculateHandler(null));
        }

        throw new IllegalArgumentException();
    }
}
