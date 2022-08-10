package nextstep.line.infra;

import nextstep.line.domain.LineRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationInspector;
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
                .flatMap(it -> it.getStations().stream())
                .collect(Collectors.toList());

        return stationIds.contains(station.getId());
    }
}
