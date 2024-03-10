package nextstep.core.subway.path.application;

import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.path.application.dto.PathCompositeWeightEdge;
import nextstep.core.subway.path.application.dto.PathFinderResponse;
import nextstep.core.subway.path.domain.PathType;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathFinder {

    private final FareCalculator fareCalculator;

    public PathFinder(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public PathFinderResponse findOptimalPath(List<Line> lines, Station departureStation, Station arrivalStation, PathType type) { // TODO: 매개변수 수 줄이기
        validateLines(lines, departureStation, arrivalStation);

        WeightedMultigraph<Station, PathCompositeWeightEdge> pathGraph = buildPathFromLines(lines, type);

        return createPathFinderResult(findOptimalPath(departureStation, arrivalStation, pathGraph));
    }

    public boolean existPathBetweenStations(List<Line> lines, Station departureStation, Station arrivalStation) {
        return hasFoundPath(findShortestPath(lines, departureStation, arrivalStation));
    }

    private GraphPath<Station, PathCompositeWeightEdge> findOptimalPath(Station departure,
                                                                        Station arrival,
                                                                        WeightedMultigraph<Station, PathCompositeWeightEdge> graph) {
        return new DijkstraShortestPath<>(graph).getPath(departure, arrival);
    }

    private WeightedMultigraph<Station, PathCompositeWeightEdge> buildPathFromLines(List<Line> lines, PathType type) {
        WeightedMultigraph<Station, PathCompositeWeightEdge> pathGraph = new WeightedMultigraph<>(PathCompositeWeightEdge.class);

        lines.forEach(line -> line.getSortedAllSections().forEach(section -> {
            buildPathFromSection(type, section, pathGraph);
        }));

        return pathGraph;
    }

    private void buildPathFromSection(PathType type, Section section, WeightedMultigraph<Station, PathCompositeWeightEdge> pathGraph) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        pathGraph.addVertex(upStation);
        pathGraph.addVertex(downStation);

        PathCompositeWeightEdge weightEdge = new PathCompositeWeightEdge(section.getDistance(), section.getDuration(), section.getLine().getAdditionalFare());
        pathGraph.addEdge(upStation, downStation, weightEdge);

        if (PathType.DISTANCE == type) {
            pathGraph.setEdgeWeight(weightEdge, section.getDistance());
        }
        if (PathType.DURATION == type) {
            pathGraph.setEdgeWeight(weightEdge, section.getDuration());
        }
    }

    private PathFinderResponse createPathFinderResult(GraphPath<Station, PathCompositeWeightEdge> path) {
        validatePath(path);

        int distance = calculateDistance(path);
        return new PathFinderResponse(
                path.getVertexList(),
                distance, calculateDuration(path),
                fareCalculator.calculateTotalFare(distance, convertAdditionalFares(path)));
    }

    private List<Integer> convertAdditionalFares(GraphPath<Station, PathCompositeWeightEdge> path) {
        return path.getEdgeList().stream()
                .map(PathCompositeWeightEdge::getAdditionalFare)
                .distinct()
                .collect(Collectors.toList());
    }

    private int calculateDistance(GraphPath<Station, PathCompositeWeightEdge> path) {
        int distance = 0;
        for (PathCompositeWeightEdge edge : path.getEdgeList()) {
            distance += edge.getDistance();
        }

        return distance;
    }

    private int calculateDuration(GraphPath<Station, PathCompositeWeightEdge> path) {
        int duration = 0;
        for (PathCompositeWeightEdge edge : path.getEdgeList()) {
            duration += edge.getDuration();
        }

        return duration;
    }

    private void validatePath(GraphPath<Station, PathCompositeWeightEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(List<Line> lines, Station departureStation, Station arrivalStation) {
        validateLines(lines, departureStation, arrivalStation);

        return findShortestPath(
                departureStation,
                arrivalStation,
                buildPathFromLines(lines, new WeightedMultigraph<>(DefaultWeightedEdge.class)));
    }

    private void validateLines(List<Line> lines, Station departureStation, Station arrivalStation) {
        if (!hasAtLeastOneSection(lines)) {
            throw new IllegalArgumentException("노선에 연결된 구간이 하나라도 존재해야 합니다.");
        }

        if (doesNotContainStation(lines, departureStation)) {
            throw new IllegalArgumentException("노선에 연결된 출발역이 아닙니다.");
        }

        if (doesNotContainStation(lines, arrivalStation)) {
            throw new IllegalArgumentException("노선에 연결된 도착역이 아닙니다.");
        }
    }

    private boolean doesNotContainStation(List<Line> lines, Station station) {
        return lines.stream()
                .noneMatch(line -> line.getAllStations().contains(station));
    }

    private boolean hasAtLeastOneSection(List<Line> lines) {
        return getTotalSectionCount(lines) > 0;
    }

    private int getTotalSectionCount(List<Line> lines) {
        return lines.stream()
                .mapToInt(line -> line.getSortedAllSections().size())
                .sum();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> buildPathFromLines(List<Line> lines,
                                                                                WeightedMultigraph<Station, DefaultWeightedEdge> path) {
        lines.forEach(line -> buildPathFromSections(path, line.getSortedAllSections()));
        return path;
    }

    private void buildPathFromSections(WeightedMultigraph<Station, DefaultWeightedEdge> path, List<Section> sections) {
        sections.forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            path.addVertex(upStation);
            path.addVertex(downStation);

            path.setEdgeWeight(path.addEdge(upStation, downStation), section.getDistance());
        });
    }

    private boolean hasFoundPath(GraphPath<Station, DefaultWeightedEdge> path) {
        return path != null;
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station departureStation,
                                                                     Station arrivalStation,
                                                                     WeightedMultigraph<Station, DefaultWeightedEdge> path) {
        return new DijkstraShortestPath<>(path).getPath(departureStation, arrivalStation);
    }
}
