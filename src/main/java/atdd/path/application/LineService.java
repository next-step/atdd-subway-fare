package atdd.path.application;

import atdd.path.application.dto.CreateEdgeRequestView;
import atdd.path.application.dto.EdgeResponseDto;
import atdd.path.domain.Edge;
import atdd.path.domain.Edges;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.EdgeRepository;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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

    public EdgeResponseDto addEdge(Long lineId, CreateEdgeRequestView view) {
        this.validate(lineId, view.getSourceId(), view.getTargetId());

        Edge savedEdge = edgeRepository.save(Edge.builder()
                .lineId(lineId)
                .sourceStationId(view.getSourceId())
                .targetStationId(view.getTargetId())
                .distance(view.getDistance())
                .elapsedTime(view.getElapsedTime())
                .build());

        return EdgeResponseDto.of(savedEdge);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);

        Edges edges = new Edges(line.getAllEdges());
        edges.removeStation(station);

        Edge newEdge = edges.findNewEdge(line.getAllEdges());
        newEdge.updateLine(line);

        line.getAllEdges().forEach(it -> {
            if (!it.hasStation(station)) {
                return;
            }
            edgeRepository.delete(it);
        });

        edgeRepository.save(newEdge);
    }

    private void validate(Long lineId, Long sourceStationId, Long targetStationId) {
        if (!lineRepository.existsById(lineId))
            throw new EntityNotFoundException(lineId + "는 존재하지 않는 Line입니다.");

        if (!stationRepository.existsById(sourceStationId)) {
            throw new EntityNotFoundException(sourceStationId + "는 존재하지 않는 Station입니다.");
        }

        if (!stationRepository.existsById(targetStationId)) {
            throw new EntityNotFoundException(sourceStationId + "는 존재하지 않는 Station입니다.");
        }
    }
}
