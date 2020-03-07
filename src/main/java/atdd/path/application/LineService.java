package atdd.path.application;

import atdd.path.application.dto.CreateEdgeRequestView;
import atdd.path.application.dto.EdgeDto;
import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.EdgeRepository;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

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

    public EdgeDto addEdge(Long lineId, CreateEdgeRequestView view) {
        lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        stationRepository.findById(view.getSourceId()).orElseThrow(RuntimeException::new);
        stationRepository.findById(view.getTargetId()).orElseThrow(RuntimeException::new);

        Edge savedEdge = edgeRepository.save(Edge.builder()
                .lineId(lineId)
                .sourceStationId(view.getSourceId())
                .targetStationId(view.getTargetId())
                .distance(view.getDistance())
                .elapsedTime(view.getElapsedTime())
                .build());

        return EdgeDto.of(savedEdge);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(RuntimeException::new);

        Edges edges = new Edges(line.getEdges());
        edges.removeStation(station);

        Edge newEdge = edges.findNewEdge(line.getEdges());
        newEdge.updateLine(line);

        line.getEdges().forEach(it -> {
            if (!it.hasStation(station)) {
                return;
            }
            edgeRepository.delete(it);
        });

        edgeRepository.save(newEdge);
    }
}
