package nextstep.subway.domain.path;

import nextstep.subway.domain.Station;

public interface PathFinder {
    Path findPath(Station source, Station target);
}
