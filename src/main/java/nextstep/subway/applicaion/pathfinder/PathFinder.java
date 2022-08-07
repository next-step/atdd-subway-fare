package nextstep.subway.applicaion.pathfinder;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;

import java.util.List;

public interface PathFinder {

    Path findPath(final List<Line> lines, final Station source, final Station target);

    PathFinder findPathFinder(final PathType pathType);

}
