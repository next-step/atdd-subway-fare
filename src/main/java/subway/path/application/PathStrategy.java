package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

public interface PathStrategy {
    void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge);
}
