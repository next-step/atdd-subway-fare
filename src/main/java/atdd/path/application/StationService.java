package atdd.path.application;

import atdd.path.domain.*;
import atdd.path.application.dto.StationTimeTableDto;
import atdd.path.repository.LineRepository;
import atdd.path.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StationService {

    private final StationRepository stationRepository;

    private final LineRepository lineRepository;

    public StationService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }


    public List<StationTimeTableDto> retrieveStationTimetable(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid station id. stationId: " + stationId));
        List<Long> lineIds = station.getLines().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Line> lines = lineRepository.findAllById(lineIds);

        return lines.stream()
                .map(it -> StationTimeTableDto.builder()
                        .lineId(it.getId())
                        .lineName(it.getName())
                        .timetables(this.getTimetables(it, stationId))
                        .build())
                .collect(Collectors.toList());
    }

    private Timetables getTimetables(Line line, Long stationId) {
        return Timetables.builder()
                .up(this.getTimetable(line, stationId, true))
                .down(this.getTimetable(line, stationId, false))
                .build();
    }


    private Long getElapsedTimeBy(Line line, Long stationId, boolean isUp) {
        List<Edge> edges = line.getEdges();

        if (!isUp) {
            Collections.reverse(edges);
        }

        Long elapsedTime = 0L;

        for (Edge edge : edges) {
            if (edge.getSourceStation().getId().equals(stationId)) {
                break;
            }

            elapsedTime += edge.getElapsedTime();
        }
        return elapsedTime;
    }

    private List<LocalTime> getTimetable(Line line, Long stationId, boolean isUp) {
        Long elapsedTime = this.getElapsedTimeBy(line, stationId, isUp);

        if (elapsedTime == 0) {
            return Collections.emptyList();
        }

        return IntStream.iterate(0, i -> i + line.getIntervalTime())
                .limit(Duration.between(line.getStartTime(), line.getEndTime()).dividedBy(line.getIntervalTime()).toMinutes())
                .mapToObj(it -> line.getStartTime().plusMinutes(it).plusMinutes(elapsedTime))
                .collect(Collectors.toList());
    }
}