package nextstep.subway.domain.service.path;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.StationLineSection;
import nextstep.subway.exception.StationLineSearchFailException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationShortestPathCalculateService {
    private final StationLineRepository stationLineRepository;

    public Boolean isExistPathBetween(Station startStation, Station destinationStation) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraphFrom(getTotalStationLineSection(), StationPathSearchRequestType.DISTANCE);

        final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation.getId(), destinationStation.getId());

        return Objects.nonNull(path);
    }

    public List<Long> getShortestPathStations(Station startStation, Station destinationStation, StationPathSearchRequestType type) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraphFrom(getTotalStationLineSection(), type);

        final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation.getId(), destinationStation.getId());

        if (Objects.isNull(path)) {
            throw new StationLineSearchFailException("there is no path between start station and destination station");
        }

        return path.getVertexList();
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraphFrom(List<StationLineSection> stationLineSections, StationPathSearchRequestType type) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stationLineSections.forEach(stationLineSection -> {
            final Long upStationId = stationLineSection.getUpStationId();
            final Long downStationId = stationLineSection.getDownStationId();
            final Number weight = type.calculateWeightOf(stationLineSection);

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), weight.doubleValue());
        });

        return graph;
    }

    private List<StationLineSection> getTotalStationLineSection() {
        return stationLineRepository.findAll()
                .stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
