package subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphBuilder {
    private final PathStrategy pathStrategy;

    public GraphBuilder(PathStrategy pathStrategy) {
        this.pathStrategy = pathStrategy;
    }

    public WeightedMultigraph<Station, SectionEdge> getGraph(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        List<Station> stations = getStations(sections);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            pathStrategy.setEdgeWeight(graph, section, sectionEdge);
        });

        return graph;
    }

    public List<Section> getPath(WeightedMultigraph<Station, SectionEdge> graph,
                                 Station sourceStation,
                                 Station targetStation) {
        try {
            final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            final List<SectionEdge> edgeList = dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList();
            return edgeList.stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }
}
