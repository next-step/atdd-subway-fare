package subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
public class MinimumTimePathFinder{  // TODO
    public PathRetrieveResponse findPath(List<Section> sections,
                                         Station sourceStation,
                                         Station targetStation) {
        validIsSameOriginStation(sourceStation, targetStation);

        List<Station> stations = getStations(sections);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getGraph(sections, stations);

        List<Station> stationsInPath = getPath(graph, sourceStation, targetStation);
        Long totalDistance = getDistanceInMinimumTimePath(stationsInPath, sections);
        Double minimumWeight = getWeightOfMinimumTimePath(graph, sourceStation, targetStation);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(stationsInPath))
                .distance(totalDistance)
                .duration(minimumWeight.longValue())
                .build();
    }

    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }

    private Double getWeightOfMinimumTimePath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                              Station sourceStation,
                                              Station targetStation) {
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private List<Station> getPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                  Station sourceStation,
                                  Station targetStation) {
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private Long getDistanceInMinimumTimePath(List<Station> stationsInPath, List<Section> sections) {
        List<Section> sectionsInPath = getSections(stationsInPath, sections);
        return sectionsInPath.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toList());
    }

    private List<Section> getSections(List<Station> stationsInPath, List<Section> sections) {
        return sections.stream()
                .filter(section -> isSectionInPath(section, stationsInPath))
                .collect(Collectors.toList());
    }

    private boolean isSectionInPath(Section section, List<Station> stationsInPath) {
        int indexOfUpStation = stationsInPath.indexOf(section.getUpStation());
        return indexOfUpStation != -1 &&
                indexOfUpStation + 1 < stationsInPath.size() &&
                stationsInPath.get(indexOfUpStation + 1).equals(section.getDownStation());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getGraph(List<Section> sections, List<Station> stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.forEach(graph::addVertex);
        sections.forEach(section ->
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDuration()));
        return graph;
    }
}
