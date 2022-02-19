package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public enum PathType {
    DISTANCE("최단 거리"),
    DURATION("최단 시간");

    PathType(String description) {

    }

    public GraphPath<Station, DefaultWeightedEdge> getPath(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath,
                                                           DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPathDuration,
                                                           Station source, Station target) {
        try {
            return getPathDistanceOrDuration(dijkstraShortestPath, dijkstraShortestPathDuration, source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException("노선에 등록되지 않은 역입니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> getPathDistanceOrDuration(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath,
                                                           DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPathDuration,
                                                           Station source, Station target) {
        if (this.name().equals("DISTANCE")) {
            return dijkstraShortestPath.getPath(source, target);
        }
        return dijkstraShortestPathDuration.getPath(source, target);
    }

    public void setEdgeWeight(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if (this.name().equals("DISTANCE")) {
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        }
        if (this.name().equals("DURATION")) {
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDuration());
            }
        }
    }

    public int getPathWeight(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath, Station source, Station target) {
        return (int)dijkstraShortestPath.getPathWeight(source, target);
    }
}
