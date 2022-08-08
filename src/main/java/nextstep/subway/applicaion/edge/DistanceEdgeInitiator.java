package nextstep.subway.applicaion.edge;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class DistanceEdgeInitiator implements EdgeInitiator {

    @Override
    public void initEdges(List<Line> lines,
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, it.getDistance());
            });
    }

    @Override
    public void initOppositeEdges(List<Line> lines,
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(),
                it.getDistance(), it.getDuration()))
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, it.getDistance());
            });
    }
}
