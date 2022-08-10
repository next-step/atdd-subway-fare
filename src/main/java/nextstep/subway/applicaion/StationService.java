package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.from(station);
    }

    public List<StationResponse> findAllStations() {
        return StationResponse.listOf(stationRepository.findAll());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public StationResponse createStationResponse(Station station) {
        return StationResponse.from(station);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public List<Station> findAllStationsById(Set<Long> stationIds) {
        return stationRepository.findAllById(stationIds);
    }
}
