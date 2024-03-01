package nextstep.subway.repository;

import nextstep.subway.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}