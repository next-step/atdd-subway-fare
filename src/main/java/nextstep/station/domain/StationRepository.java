package nextstep.station.domain;

import nextstep.exception.StationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station findStation(Long id) {
        return findById(id).orElseThrow(StationNotFoundException::new);
    }

}