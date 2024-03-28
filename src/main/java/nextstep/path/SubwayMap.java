package nextstep.path;

import nextstep.line.Line;
import nextstep.path.fare.Fare;
import nextstep.station.Station;

import java.util.List;

public class SubwayMap {

    private PathFinder pathFinder;
    private Station sourceStation;
    private Station targetStation;

    public SubwayMap(List<Line> lines, Station sourceStation, Station targetStation) {
        this.pathFinder = new PathFinder(lines);
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Path findPath(PathType type) {
        return pathFinder.findPath(sourceStation, targetStation, type);
    }

    public int calculateFare() {
        int shortestDistance = pathFinder.getShortestDistance(sourceStation, targetStation);
        return Fare.calculate(shortestDistance);
    }
}
