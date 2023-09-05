package nextstep.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import nextstep.subway.domain.Station;
import nextstep.subway.dto.StationRequest;
import nextstep.subway.exception.impl.StationNotFoundException;
import nextstep.subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(Station.from(stationRequest));
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
