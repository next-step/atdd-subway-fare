package nextstep.subway.path;

import nextstep.subway.Exception.ErrorCode;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.section.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
    private final List<Line> lines;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse shortestPath(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "출발역과 도착역이 같습니다.");
        }

        List<Section> sections = allSections();
        List<Station> stations = allStations(sections);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeWeightedMultigraph(stations, sections);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

        if (shortestPath == null) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "연결되지 않은 역 정보입니다.");
        }
        return new PathResponse(shortestPath.getVertexList(), (long) dijkstraShortestPath.getPathWeight(sourceStation, targetStation));
    }

    public NewPathResponse shortestPath(Station sourceStation, Station targetStation, String type) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "출발역과 도착역이 같습니다.");
        }

        List<Section> sections = allSections();
        List<Station> stations = allStations(sections);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeWeightedMultigraph(stations, sections, type);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

        if (shortestPath == null) {
            throw new SubwayException(ErrorCode.CANNOT_FIND_SHORTEST_PATH, "연결되지 않은 역 정보입니다.");
        }
        return new NewPathResponse(shortestPath.getVertexList(), (long) dijkstraShortestPath.getPathWeight(sourceStation, targetStation), 0L);
    }

    private List<Section> allSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().get().stream())
                .collect(Collectors.toList());
    }

    private static List<Station> allStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> section.stations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> makeWeightedMultigraph(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
        return graph;
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> makeWeightedMultigraph(List<Station> stations, List<Section> sections, String type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);
        if (type.equals("DISTANCE")){
            sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
        }
        if (type.equals("DURATION")){
            sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDuration()));
        }
        return graph;
    }

    public void isConnected(Station sourceStation, Station targetStation) {
        shortestPath(sourceStation, targetStation);
    }
}
