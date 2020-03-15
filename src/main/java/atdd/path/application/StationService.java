package atdd.path.application;

import atdd.path.application.dto.StationTimetableDto;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.domain.Timetables;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    private final LineRepository lineRepository;

    public StationService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public List<StationTimetableDto> retrieveStationTimetable(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station id. stationId: " + stationId));
        List<Long> lineIds = station.getLinesByEdge().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        List<Line> lines = lineRepository.findAllById(lineIds);

        return lines.stream()
                .map(it -> StationTimetableDto.builder()
                        .lineId(it.getId())
                        .lineName(it.getName())
                        .timetables(Timetables.of(it, stationId))
                        .build())
                .collect(Collectors.toList());
    }
}