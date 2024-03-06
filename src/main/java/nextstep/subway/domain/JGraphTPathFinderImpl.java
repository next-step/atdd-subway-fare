package nextstep.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class JGraphTPathFinderImpl extends PathFinder {

    public JGraphTPathFinderImpl(FareCalculator fareCalculator) {
        super(fareCalculator);
    }

    @Override
    protected PathResponse getPath(PathRequest pathRequest, List<Line> lines) {
        final Set<Section> sections = getAllSectionsInLines(lines);
        final WeightedMultigraph<String, DefaultWeightedEdge> sectionGraph = getWightedGraphWithSection(sections, pathRequest.getType());

        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(sectionGraph);

        final GraphPath<String, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(
            pathRequest.getSource().toString(), pathRequest.getTarget().toString());

        if (graphPath == null) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }

        return getPathResponse(sections, Set.copyOf(graphPath.getVertexList()));
    }

    private PathResponse getPathResponse(Set<Section> sections, Set<String> stationIds) {
        final Map<String, Station> stationMap = getIdToStationMap(sections);
        final List<Section> edgeSection = getSectionsComposingEdges(sections, stationIds);

        final List<Station> stationsInPath = stationIds.stream().map(stationMap::get).collect(Collectors.toList());
        final Set<Line> linesInPath = edgeSection.stream().map(Section::getLine).collect(Collectors.toSet());

        final int distance = edgeSection.stream().map(Section::getDistance).reduce(0, Integer::sum);
        final int duration = edgeSection.stream().map(Section::getDuration).reduce(0, Integer::sum);

        return new PathResponse(
            stationsInPath,
            linesInPath,
            distance,
            duration
        );
    }

    private static List<Section> getSectionsComposingEdges(Set<Section> sections, Set<String> stationIds) {
        return sections.stream().filter(section ->
            stationIds.contains(section.getUpStation().getId().toString())
                && stationIds.contains(section.getDownStation().getId().toString()
            )).collect(Collectors.toList());
    }

    private Map<String, Station> getIdToStationMap(Set<Section> sections) {
        final Map<String, Station> stationMap = new HashMap<>();

        sections.forEach(section -> {
            stationMap.put(section.getUpStation().getId().toString(), section.getUpStation());
            stationMap.put(section.getDownStation().getId().toString(), section.getDownStation());
        });

        return stationMap;
    }

    private static WeightedMultigraph<String, DefaultWeightedEdge> getWightedGraphWithSection(
        Set<Section> sections,
        PathSearchType type
    ) {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        sections.forEach(section -> {
            final String upStationId = section.getUpStation().getId().toString();
            final String downStationId = section.getDownStation().getId().toString();

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);

            graph.setEdgeWeight(edge, type.getWeight(section));
        });

        return graph;
    }

    private Set<Section> getAllSectionsInLines(List<Line> lines) {
        return lines.stream().flatMap(line -> line.getSections().stream())
            .collect(Collectors.toSet());
    }
}
