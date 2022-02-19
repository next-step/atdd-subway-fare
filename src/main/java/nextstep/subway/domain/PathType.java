package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.function.Function;

public enum PathType {
    DISTANCE("최단 거리", Section::getDistance),
    DURATION("최단 시간", Section::getDuration);

    private String description;
    private Function<Section, Integer> expression;

    PathType(String description, Function<Section, Integer> expression) {
        this.description = description;
        this.expression = expression;
    }

    public GraphPath<Station, DefaultWeightedEdge> getPath(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath,
                                                           Station source, Station target) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException("노선에 등록되지 않은 역입니다.");
        }
    }

    public void setEdgeWeight(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), expression.apply(section));
        }
    }

    public int getPathWeight(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath, Station source, Station target) {
        return (int)dijkstraShortestPath.getPathWeight(source, target);
    }
}
