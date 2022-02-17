package nextstep.subway.domain.map.pathsearch;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SubwayMap;

public interface PathSearchStrategy {
    Path find(SubwayMap subwayMap, List<Line> lines, Station upStation, Station downStation);
}
