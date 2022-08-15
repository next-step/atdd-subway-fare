package nextstep.subway.applicaion;

import nextstep.subway.domain.PathType;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;

public abstract class SubwayPathFinder {

    protected final SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
    protected final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    protected void setGraph(PathType type) {
        addVertex();
        setEdgeWeight(type);
        setOppositeEdgesWeight(type);
    }

    protected GraphPath<Station, SectionEdge> getPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target);
    }

    protected List<SectionEdge> getEdgeList(Station source, Station target) {
        return getPath(source, target).getEdgeList();
    }

    protected abstract void addVertex();
    protected abstract void setEdgeWeight(PathType type);
    protected abstract void setOppositeEdgesWeight(PathType type);

}
