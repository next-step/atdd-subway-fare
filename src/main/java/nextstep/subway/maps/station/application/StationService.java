package nextstep.subway.maps.station.application;

import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.domain.StationRepository;
import nextstep.subway.maps.station.dto.StationCreateRequest;
import nextstep.subway.maps.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Map<Long, Station> findStationsByIds(List<Long> ids) {
        return stationRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(it -> it.getId(), Function.identity()));
    }

    public StationResponse saveStation(StationCreateRequest request) {
        Station station = stationRepository.save(request.toStation());
        return StationResponse.of(station);
    }

    public List<StationResponse> findStations() {
        List<Station> stations = stationRepository.findAll();
        return StationResponse.listOf(stations);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
