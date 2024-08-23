package nextstep.path.domain;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.entity.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.*;
import java.util.stream.Collectors;

import static nextstep.common.constant.ErrorCode.PATH_DUPLICATE_STATION;
import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;

public class GraphModel {
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph;
    private Map<DefaultWeightedEdge, Section> edgeToSectionMap = new HashMap<>();
    private Long source;
    private Long target;

    public GraphModel(Long source, Long target) {
        validateDuplicate(source, target);
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.source = source;
        this.target = target;
    }

    public static GraphModel of(final Long source, final Long target) {
        return new GraphModel(source, target);
    }

    public Path findPath(final Member member, final List<Line> lines, final String type) {
        createGraphModel(lines, type);
        GraphPath<Long, DefaultWeightedEdge> graphPath = findShortestPath();
        List<Station> stations = getStations(lines, graphPath.getVertexList());
        Sections sections = getSections(graphPath.getEdgeList());

        return Path.of(member, lines, stations, sections);
    }

    public void createGraphModel(final List<Line> lines, final String type) {
        if (lines.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        for (Line line : lines) {
            addSectionsToGraph(line, type);
        }

        containsVertex(source);
        containsVertex(target);
    }

    public void addSectionsToGraph(final Line line, final String type) {
        List<Section> sectionList = line.getSections().getSections();

        if (sectionList.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
        for (Section section : sectionList) {
            addEdge(section.getUpStation().getId(), section.getDownStation().getId(), section, type);
        }
    }

    public void addEdge(final Long newSource, final Long newTarget, final Section section, final String type) {
        validateDuplicate(newSource, newTarget);
        double weight = section.getWeight(type);

        graph.addVertex(newSource);
        graph.addVertex(newTarget);
        DefaultWeightedEdge defaultWeightedEdge = graph.addEdge(newSource, newTarget);
        graph.setEdgeWeight(defaultWeightedEdge, weight);
        edgeToSectionMap.put(defaultWeightedEdge, section);
    }

    private GraphPath<Long, DefaultWeightedEdge> findShortestPath() {
        validateDuplicate(source, target);
        DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> graphPath = shortestPath.getPath(source, target);

        if (graphPath.getVertexList() == null || graphPath.getVertexList().isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        return graphPath;

    }

    public List<Station> getStations(final List<Line> lines, final List<Long> stationIds) {
        List<Station> stationList = new ArrayList<>();
        for (Long stationId : stationIds) {
            Station station = getStation(lines, stationId);
            stationList.add(station);
        }

        return stationList;
    }

    private Sections getSections(List<DefaultWeightedEdge> defaultWeightedEdges) {
        List<Section> sections = defaultWeightedEdges.stream()
                .map(edgeToSectionMap::get)
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    private Long getTotalDistance(List<Section> sections) {
        return sections.stream()
                .mapToLong(Section::getDistance)
                .sum();
    }

    private Long getTotalDuration(List<Section> sections) {
        return sections.stream()
                .mapToLong(Section::getDuration)
                .sum();
    }

    public Station getStation(final List<Line> lines, final Long stationId) {
        return findStationInLines(lines, stationId)
                .orElseThrow(() -> new PathException(String.valueOf(PATH_NOT_FOUND)));
    }

    private Optional<Station> findStationInLines(final List<Line> lines, final Long stationId) {
        return lines.stream()
                .map(line -> findStationInLine(line, stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Station> findStationInLine(final Line line, final Long stationId) {
        List<Section> sectionList = line.getSections().getSections();
        return findStationInSections(sectionList, stationId);
    }

    private Optional<Station> findStationInSections(final List<Section> sections, final Long stationId) {
        return sections.stream()
                .map(section -> findStationInSection(section, stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Station> findStationInSection(final Section section, final Long stationId) {
        if (section.getUpStation().getId().equals(stationId)) {
            return Optional.ofNullable(section.getUpStation());
        }
        if (section.getDownStation().getId().equals(stationId)) {
            return Optional.ofNullable(section.getDownStation());
        }
        return Optional.empty();
    }

    public void containsVertex(final Long vertexId) {
        if (!graph.containsVertex(vertexId)) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }
    }

    public void validateDuplicate(final Long source, final Long target) {
        if (source.equals(target)) {
            throw new PathException(String.valueOf(PATH_DUPLICATE_STATION));
        }
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> getGraph() {
        return this.graph;
    }
}

