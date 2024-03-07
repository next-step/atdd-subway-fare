package nextstep.subway.path;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.Station;
import org.jgrapht.graph.WeightedMultigraph;

public interface PathFinderStrategy {
    void addEdge(WeightedMultigraph<Station, PathWeightEdge> graph, Section section);
}
