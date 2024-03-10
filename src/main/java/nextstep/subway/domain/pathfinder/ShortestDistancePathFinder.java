package nextstep.subway.domain.pathfinder;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;

import java.util.List;

public class ShortestDistancePathFinder {
    private PathFinder pathFinder;

    public ShortestDistancePathFinder(List<Line> lineList) {
        this.pathFinder = new PathFinder(lineList, PathType.DISTANCE);
    }

    public Path findPath(Station source, Station target) {
        return pathFinder.findPath(source, target, PathType.DISTANCE);
    }
}
