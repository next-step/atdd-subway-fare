package nextstep.subway.path.strategy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.PathWeightEdge;
import nextstep.subway.station.Station;
import org.jgrapht.graph.WeightedMultigraph;

public interface PathFinderStrategy {
    void addEdge(WeightedMultigraph<Station, PathWeightEdge> graph, Section section);
}
