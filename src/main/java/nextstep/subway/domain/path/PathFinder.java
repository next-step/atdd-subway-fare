package nextstep.subway.domain.path;

import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public abstract class PathFinder {

    protected WeightedMultigraph<Station, SectionEdge> graph;
    protected GraphPath path;

    public PathFinder() {
        graph = new WeightedMultigraph<>(SectionEdge.class);
    }

    public Optional<GraphPath> findPath(Station source, Station target, List<Section> edges) {
        validateStations(source, target);

        setUpWeightedEdges(edges);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        path = dijkstraShortestPath.getPath(source, target);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target));
    }

    private void validateStations(Station source, Station target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("출발역: " + source.getName() + ", 도착역: " + target.getName() + "이 유효한 역이 아닙니다.");
        }

        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 서로 같을 수 없습니다");
        }
    }

    abstract void setUpWeightedEdges(List<Section> edges);

    public abstract Long getDistance();

    public abstract Long getTransitTime();
}
