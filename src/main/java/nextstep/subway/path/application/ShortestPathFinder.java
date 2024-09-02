package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShortestPathFinder implements PathFinder {

    @Override
    public List<LineSection> find(List<Line> lines, Station source, Station target, PathType pathType) {

        validateSourceAndTargetStations(source, target);

        WeightedMultigraph<Station, SectionEdge> graph = createGraph(lines, pathType);
        GraphPath<Station, SectionEdge> path = findShortestPath(source, target, graph);

        return path.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }

    private void validateSourceAndTargetStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException(SubwayExceptionType.SOURCE_AND_TARGET_SAME);
        }
    }

    private WeightedMultigraph<Station, SectionEdge> createGraph(List<Line> lines, PathType pathType) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addEdge(lines, graph, pathType);
        return graph;
    }

    private void addEdge(List<Line> lines, WeightedMultigraph<Station, SectionEdge> graph, PathType pathType) {
        lines.stream()
            .flatMap(line -> line.getLineSections().stream())
            .forEach(section -> {
                Station upStation = section.getUpStation();
                Station downStation = section.getDownStation();
                graph.addVertex(upStation);
                graph.addVertex(downStation);

                SectionEdge edge = new SectionEdge(section);
                graph.addEdge(upStation, downStation, edge);

                if (pathType == PathType.DISTANCE) {
                    graph.setEdgeWeight(edge, section.getDistance());
                    return;
                }
                graph.setEdgeWeight(edge, section.getDuration());
            });
    }

    private GraphPath<Station, SectionEdge> findShortestPath(
        Station source, Station target, WeightedMultigraph<Station, SectionEdge> graph) {

        validateVerticesExist(graph, source, target);

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw new SubwayException(SubwayExceptionType.PATH_NOT_FOUND);
        }
        return path;
    }

    private void validateVerticesExist(WeightedMultigraph<Station, SectionEdge> graph, Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new SubwayException(SubwayExceptionType.PATH_NOT_FOUND);
        }
    }
}
