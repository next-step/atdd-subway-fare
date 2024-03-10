package nextstep.path;

import nextstep.line.Line;
import nextstep.station.Station;

import java.util.List;

public enum PathType {

    DISTANCE(new DistancePathFinder()), DURATION(new DurationPathFinder());

    private final PathFinder pathFinder;

    PathType(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public Path findPath(List<Line> lines, Station source, Station target) {
        return pathFinder.findPath(lines, source, target);
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }
}
