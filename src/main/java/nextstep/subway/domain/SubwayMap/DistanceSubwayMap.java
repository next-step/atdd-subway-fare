package nextstep.subway.domain.SubwayMap;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.function.Consumer;

public class DistanceSubwayMap extends SubwayMap {
    public DistanceSubwayMap(List<Line> lines) {
        super(lines);
    }

    @Override
    protected GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                getGraph(() -> new SimpleDirectedWeightedGraph<>(SectionEdge.class)));
        return dijkstraShortestPath.getPath(source, target);
    }

    @Override
    protected Consumer<Section> getConnectSectionConsumer(AbstractBaseGraph<Station, SectionEdge> graph) {
        return it -> {
            SectionEdge sectionEdge = SectionEdge.of(it);
            graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, it.getDistance());
        };
    }

}
