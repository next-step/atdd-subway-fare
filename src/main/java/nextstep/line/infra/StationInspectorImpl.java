package nextstep.line.infra;

import nextstep.station.domain.Station;
import nextstep.station.domain.StationInspector;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationInspectorImpl implements StationInspector {
    private final LineRepository lineRepository;

    public StationInspectorImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean belongsToLine(Station station) {
        List<Long> stationIds = lineRepository.findAll()
                .stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return stationIds.contains(station.getId());
    }
}
