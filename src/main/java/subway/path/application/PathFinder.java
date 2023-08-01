package subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.domain.Station;

import java.util.List;

public class PathFinder {  // TODO
    private final PathStrategy strategy;

    // strategy를 constructor에서 주입
    public PathFinder(PathStrategy strategy) {
        this.strategy = strategy;
    }

    public PathRetrieveResponse findPath(List<Section> sections,
                                         Station sourceStation,
                                         Station targetStation) {
        return strategy.findPath(sections, sourceStation, targetStation);
    }

    // TODO: 나머지 메서드들...

    private WeightedMultigraph<Station, DefaultWeightedEdge> getGraph(List<Section> sections, List<Station> stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            strategy.setEdgeWeight(graph, section, edge);
        });

        return graph;
    }
}
