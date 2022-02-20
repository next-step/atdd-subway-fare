package nextstep.subway.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Query("SELECT l FROM Line l LEFT JOIN FETCH l.sections")
    List<Line> findAllWithSections();
}
