package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static nextstep.subway.ui.exception.ExceptionMessage.NOT_EXISTS_STATIONS_IN_LINE;
import static nextstep.subway.ui.exception.ExceptionMessage.NO_CONNECTION_START_AND_END_STATION;

public class PathFinder {
    public Path shortsPath(List<Line> lines, Station source, Station target, PathType type) {
        return new Path(shortsPathSections(lines, source, target, type));
    }

    private List<Section> shortsPathSections(List<Line> lines, Station source, Station target, PathType type) {
        GraphPath<Station, SectionEdge> path = getPath(lines, source, target, type);
        if (path == null) {
            throw new PathException(NO_CONNECTION_START_AND_END_STATION.getMsg());
        }
        return path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }

    private GraphPath<Station, SectionEdge> getPath(List<Line> lines, Station source, Station target, PathType type) {
        try {
            return createDijkstraShortestPath(lines, type).getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException(NOT_EXISTS_STATIONS_IN_LINE.getMsg());
        }
    }

    private DijkstraShortestPath<Station, SectionEdge> createDijkstraShortestPath(List<Line> lines, PathType type) {
        Lines lineGroup = new Lines(lines);
        Set<Station> stations = lineGroup.getStations();
        List<Section> sections = lineGroup.getSections();

        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertex(stations, graph);
        setEdgeWeight(sections, graph, type);

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(Set<Station> stations, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdgeWeight(List<Section> sections, WeightedMultigraph<Station, SectionEdge> graph, PathType type) {
        for (Section section : sections) {
            // Edge factory failed
//            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), type.weight(sectionEdge));
            SectionEdge sectionEdge = SectionEdge.valueOf(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, type.weight(sectionEdge));
        }
    }
}
