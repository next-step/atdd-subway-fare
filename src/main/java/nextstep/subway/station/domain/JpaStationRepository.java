package nextstep.subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface JpaStationRepository extends StationRepository, JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    @Override
    List<Station> findAllById(Iterable<Long> stationIds);

}
