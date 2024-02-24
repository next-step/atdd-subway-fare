package nextstep.subway.domain.strategy;

import nextstep.subway.domain.Station;

import java.util.List;

public interface ShortestPathStrategy {
    List<Station> findShortestPath(Station source, Station target);
    int findShortestValue(Station source, Station target);
}
