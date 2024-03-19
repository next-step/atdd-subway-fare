package nextstep.path;

import nextstep.station.Station;

public class Path {

    private PathFinder pathFinder;
    private Station sourceStation;
    private Station targetStation;
    private PathType type;

    public Path(PathFinder pathFinder, Station sourceStation, Station targetStation) {
        this.pathFinder = pathFinder;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.type = PathType.DISTANCE;
    }

    public Path(PathFinder pathFinder, Station sourceStation, Station targetStation, PathType type) {
        this(pathFinder, sourceStation, targetStation);
        this.type = type;
    }

    public PathResponse findPath(){
        PathResponse pathResponse = pathFinder.findPath(sourceStation, targetStation, type);
        return pathResponse;
    }

    public void isValidateRoute(){
        pathFinder.isValidateRoute(sourceStation, targetStation, type);
    }
}
