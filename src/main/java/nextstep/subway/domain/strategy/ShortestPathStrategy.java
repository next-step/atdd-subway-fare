package nextstep.subway.domain.strategy;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.util.List;

public interface ShortestPathStrategy {
    List<Station> findShortestPath(Station source, Station target);
    List<Section> findEdges(Station source, Station target);
    long findShortestValue(Station source, Station target);
}
