package nextstep.subway.path;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class DurationPathFinderStrategy implements PathFinderStrategy {

    @Override
    public void addEdge(WeightedMultigraph<Station, PathWeightEdge> graph, Section section) {
        PathWeightEdge edge = new PathWeightEdge(section.getDistance(), section.getDuration());
        graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        graph.setEdgeWeight(edge, section.getDuration());
    }
}
