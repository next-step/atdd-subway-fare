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

    public abstract Long getFare();

    long calculateFare(long distance) {
        long fare = 1250L;
        if (distance <= 10) {
            return fare;
        }

        if (distance > 10 && distance <= 50) {
            long until50km = distance - 10;
            fare += (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);
        }

        if (distance > 50) {
            // 50km까지 요금
            long until50km = 40;
            fare += (long) ((Math.ceil((until50km - 1) / 5) + 1) * 100);

            // 50km 초과 요금
            long over50km = distance - 50;
            fare += (long) ((Math.ceil((over50km - 1) / 8) + 1) * 100);
        }

        return fare;
    }
}
