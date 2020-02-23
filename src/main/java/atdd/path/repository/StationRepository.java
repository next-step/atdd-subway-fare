package atdd.path.repository;

import atdd.path.domain.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StationRepository extends CrudRepository<Station, Long> {
    Optional<Station> findByName(String name);
}
