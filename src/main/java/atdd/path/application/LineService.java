package atdd.path.application;

import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.EdgeRepository;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineService {
    private final EdgeRepository edgeRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(EdgeRepository edgeRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.edgeRepository = edgeRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void addEdge(Long lineId, Long sourceId, Long targetId, int distance, int elapsedMinutes) {
        lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        stationRepository.findById(sourceId).orElseThrow(RuntimeException::new);
        stationRepository.findById(targetId).orElseThrow(RuntimeException::new);

        Edge savedEdge = edgeRepository.save(Edge.builder()
                .lineId(lineId)
                .sourceStationId(sourceId)
                .targetStationId(targetId)
                .distance(distance)
                .elapsedMinutes(elapsedMinutes)
                .build());
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(RuntimeException::new);
        Station station = stationRepository.findById(stationId)
                .orElseThrow(RuntimeException::new);

        List<Edge> oldEdges = line.getEdges();
        Edges edges = new Edges(line.getEdges());
        edges.removeStation(station);

        Edge newEdge = edges.getEdges().stream()
                .filter(it -> !oldEdges.contains(it))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        newEdge.updateLine(line);

        line.getEdges().stream()
                .filter(it -> it.hasStation(station))
                .forEach(it -> edgeRepository.delete(it));
        edgeRepository.save(newEdge);
    }
}
