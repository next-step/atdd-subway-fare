package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    List<LineSection> find(List<Line> lines, Station source, Station target, PathType pathType);
}
