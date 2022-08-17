package nextstep.subway.domain.path.finder;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;

public interface PathFinder {

  Path findPath(Station source, Station target, PathType pathType, int age);
}
