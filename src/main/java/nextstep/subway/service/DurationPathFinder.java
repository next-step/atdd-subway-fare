package nextstep.subway.service;

import nextstep.subway.dto.path.PathEdge;
import nextstep.subway.entity.Section;
import nextstep.subway.entity.Sections;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class DurationPathFinder extends PathFinder {
    public DurationPathFinder(List<Sections> sectionsList) {
        super(sectionsList);
    }

    @Override
    protected void setEdgeWeight(Section section) {
        PathEdge edge = new PathEdge(section.getDistance(), section.getDuration());
        graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        graph.setEdgeWeight(edge, section.getDuration());
    }
}
