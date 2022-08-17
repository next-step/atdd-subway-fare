package nextstep.subway.util.subwaymap;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;

import java.util.List;

public interface SubwayMap {
    PathType pathType();

    Path path(List<Line> lines, Station source, Station target);

    Path shortestPath(List<Line> lines, Station source, Station target);

    boolean isDefaultPathShortest();
}
