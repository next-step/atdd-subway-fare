package nextstep.subway.util;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShortestDistanceSubwayMap implements SubwayMap {
    @Override
    public PathType pathType() {
        return PathType.DISTANCE;
    }

    @Override
    public Path findPath(List<Line> lines, Station source, Station target) {
        return findShortestPath(lines, source, target);
    }
}
