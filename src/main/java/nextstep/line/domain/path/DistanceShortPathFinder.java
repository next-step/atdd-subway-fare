package nextstep.line.domain.path;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class DistanceShortPathFinder extends ShortPathFinder {

    public DistanceShortPathFinder(List<Station> stations, List<Section> sections) {
        super(stations, sections);
    }

    @Override
    public boolean isSupport(ShortPathType type) {
        return type.isDistance();
    }

    @Override
    void setGraphEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), section);
            graph.setEdgeWeight(section, section.getDistance());
        }
    }


}
