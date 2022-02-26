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
    private final Lines lines;
    private final PathType type;

    public PathFinder(List<Line> lines, PathType type) {
        this.lines = new Lines(lines);
        this.type = type;
    }

    public Path shortsPath(Station source, Station target) {
        return new Path(shortsPathSections(source, target), shortsPathStations(source, target));
    }

    private List<Section> shortsPathSections(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = getPath(source, target);
        if (path == null) {
            throw new PathException(NO_CONNECTION_START_AND_END_STATION.getMsg());
        }
        return path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }

    private List<Station> shortsPathStations(Station source, Station target) {
        GraphPath<Station, SectionEdge> path = getPath(source, target);
        if (path == null) {
            throw new PathException(NO_CONNECTION_START_AND_END_STATION.getMsg());
        }
        return path.getVertexList();
    }

    private GraphPath<Station, SectionEdge> getPath(Station source, Station target) {
        try {
            return createDijkstraShortestPath().getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException(NOT_EXISTS_STATIONS_IN_LINE.getMsg());
        }
    }

    private DijkstraShortestPath<Station, SectionEdge> createDijkstraShortestPath() {
        Set<Station> stations = lines.getStations();
        List<Section> sections = lines.getSections();

        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addVertex(stations, graph);
        setEdgeWeight(sections, graph);

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(Set<Station> stations, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdgeWeight(List<Section> sections, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Section section : sections) {
            SectionEdge sectionEdge = SectionEdge.valueOf(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, type.weight(sectionEdge));
        }
    }
}
