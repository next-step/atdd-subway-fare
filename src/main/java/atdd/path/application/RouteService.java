package atdd.path.application;

import atdd.path.application.dto.RouteResponseDto;
import atdd.path.application.dto.StationResponseDto;
import atdd.path.domain.Graph;
import atdd.path.domain.Station;
import atdd.path.repository.LineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final LineRepository lineRepository;

    public RouteService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public RouteResponseDto findShortestDistancePath(Long startId, Long endId) {
        Graph graph = new Graph(lineRepository.findAll());
        List<Station> paths = graph.getShortestDistancePath(startId, endId);

        return RouteResponseDto.builder()
                .startStationId(startId)
                .endStationId(endId)
                .stations(StationResponseDto.listOf(paths))
                .estimatedTime(graph.getEstimatedTime(startId, endId))
                .build();
    }

    public RouteResponseDto findShortestTimePath(Long startId, Long endId) {
        Graph graph = new Graph(lineRepository.findAll());
        List<Station> paths = graph.getShortestTimePath(startId, endId);

        return RouteResponseDto.builder()
                .startStationId(startId)
                .endStationId(endId)
                .stations(StationResponseDto.listOf(paths))
                .estimatedTime(graph.getEstimatedTime(startId, endId))
                .build();
    }
}
