package nextstep.subway.domain;

import java.util.List;

public abstract class SubwayMap {
    List<Line> lines;

    public static SubwayMap create(List<Line> lines, PathType pathType) {
        if (pathType == PathType.DISTANCE) {
            return new SubwayDistanceMap(lines);
        } else {
            return new SubwayDurationMap(lines);
        }
    }

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public abstract Path findPath(Station source, Station target);

}
