package nextstep.subway.util;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;

import java.util.List;

public interface SubwayMap {
    PathType pathType();
    Path findPath(List<Line> lines, Station source, Station target);
}
