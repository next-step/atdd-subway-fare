package subway.path.domain.strategy;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.graph.SectionEdge;
import subway.station.domain.Station;

public interface PathFinderStrategy {
    void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge);
}
