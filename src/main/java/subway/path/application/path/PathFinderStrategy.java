package subway.path.application.path;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

public interface PathFinderStrategy {
    void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge);
}
