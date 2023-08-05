package subway.path.application.path;


import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

public class ShortestDistancePathFinder implements PathFinderStrategy {

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge) {
        graph.setEdgeWeight(edge, section.getDistance());
    }
}
