package nextstep.subway.domain.pathfinder;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;

import java.util.List;


public class ShortestDurationPathFinder {

    private PathFinder pathFinder;

    public ShortestDurationPathFinder(List<Line> lineList) {
        this.pathFinder = new PathFinder(lineList, PathType.DURATION);
    }

    public Path findPath(Station source, Station target) {
        return pathFinder.findPath(source, target, PathType.DURATION);
    }

}
