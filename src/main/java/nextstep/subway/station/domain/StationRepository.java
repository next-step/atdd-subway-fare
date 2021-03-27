package nextstep.subway.station.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StationRepository {
    Station save(Station toStation);

    List<Station> findAll();

    void deleteById(Long id);

    Optional<Station> findById(Long id);

    List<Station> findAllById(Iterable<Long> ids);
}
