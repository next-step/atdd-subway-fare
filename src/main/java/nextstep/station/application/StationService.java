package nextstep.station.application;

import nextstep.station.application.dto.StationRequest;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationInspector;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.CantDeleteStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;
    private final StationInspector stationInspector;

    public StationService(StationRepository stationRepository, StationInspector stationInspector) {
        this.stationRepository = stationRepository;
        this.stationInspector = stationInspector;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(stationRequest.toEntity());
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Station> findAllStationsById(List<Long> stationIds) {
        return stationRepository.findAllById(stationIds)
                .stream()
                .sorted(comparing(it -> stationIds.indexOf(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        Station station = findById(id);
        if (stationInspector.belongsToLine(station)) {
            throw new CantDeleteStationException();
        }

        stationRepository.deleteById(id);
    }
}
