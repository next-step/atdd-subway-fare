package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.section.domain.Section;
import nextstep.subway.line.section.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JGraphPathFinder implements PathFinder {
    @Override
    public Path shortcut(Lines lines,
                         Station source,
                         Station target,
                         PathType type) {
        validCorrect(lines, source, target);
        GraphPath path = createShortestPath(lines, source, target, type);
        List<Station> shortestPath = path.getVertexList();
        Double shortestValue = path.getWeight();
        Long value = lines.calculateValue(shortestPath, type);
        return new Path(shortestPath, shortestValue, value, type);
    }

    @Override
    public void validCorrect(Lines lines,
                                  Station source,
                                  Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        if (!lines.existStation(source) || !lines.existStation(target)) {
            throw new IllegalArgumentException("입력한 역을 찾을 수 없습니다.");
        }
        createShortestPath(lines, source, target, PathType.DISTANCE);
    }

    private GraphPath createShortestPath(Lines lines,
                                         Station source,
                                         Station target,
                                         PathType type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.forEach(line -> createPath(graph, line.getSections(), line.getStations(), type));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target)).orElseThrow(() -> new IllegalArgumentException("출발역과 도착역은 연결되어 있어야 합니다."));
    }

    private void createPath(WeightedGraph graph,
                            Sections sections,
                            Stations stations,
                            PathType type) {
        stations.forEach(graph::addVertex);
        sections.forEach(section -> setEdge(graph, section, type));
    }

    private static void setEdge(WeightedGraph graph,
                                Section section,
                                PathType type) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), type.findBy(section));
    }
}
