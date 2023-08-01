package subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPathFinder {
    protected List<Section> getSections(List<Station> stationsInPath, List<Section> sections) {
        return sections.stream()
                .filter(section -> isSectionInPath(section, stationsInPath))
                .collect(Collectors.toList());
    }

    protected Double getWeightOfPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                     Station sourceStation,
                                     Station targetStation) {
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private boolean isSectionInPath(Section section, List<Station> stationsInPath) {
        int indexOfUpStation = stationsInPath.indexOf(section.getUpStation());
        return indexOfUpStation != -1 &&
                indexOfUpStation + 1 < stationsInPath.size() &&
                stationsInPath.get(indexOfUpStation + 1).equals(section.getDownStation());
    }
}
