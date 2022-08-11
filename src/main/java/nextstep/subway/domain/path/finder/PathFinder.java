package nextstep.subway.domain.path.finder;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;

public interface PathFinder {

  Path findPath(Station source, Station target);
}
