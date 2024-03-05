package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.path.DistanceFare;
import nextstep.path.domain.dto.PathsDto;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;


public abstract class PathFinder {

    protected final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);

    protected PathFinder(List<Line> lines) {
        for (Line line : lines) {
            line.getStations()
                    .stream()
                    .distinct()
                    .forEach(this::addVertex);

            line.getLineSections()
                    .forEach(this::addEdge);
        }
    }


    public PathsDto findPath(Station start, Station end) {
        if (start.equals(end)) {
            throw new IllegalStateException("시작과 끝이 같은 역입니다");
        }
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(start, end);
        int duration = getDuration(path);
        int distance = getDistance(path);
        return new PathsDto(distance, duration, dijkstraShortestPath.getPath(start, end).getVertexList());
    }

    public boolean isConnected(Station start, Station end) {
        if (start.equals(end)) {
            throw new IllegalStateException("시작과 끝이 같은 역입니다");
        }
        try {
            return Optional.ofNullable(DijkstraShortestPath.findPathBetween(graph, start, end))
                    .isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void addVertex(Station station) {
        graph.addVertex(station);
    }

    protected abstract void addEdge(Section section);

    private int getDistance(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(SectionEdge.class::cast)
                .mapToInt(SectionEdge::getDistance)
                .sum();
    }

    private int getDuration(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(SectionEdge.class::cast)
                .mapToInt(SectionEdge::getDuration)
                .sum();
    }
}
