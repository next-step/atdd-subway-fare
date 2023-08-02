package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

@Component
public class MinimumTimePathFinder implements PathStrategy {

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge) {
        graph.setEdgeWeight(edge, section.getDuration());
    }
}
