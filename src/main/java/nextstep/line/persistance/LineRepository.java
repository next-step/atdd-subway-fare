package nextstep.line.persistance;


import nextstep.line.domain.Line;
import nextstep.line.domain.LineSections;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {
    List<Line> findByLineSections(LineSections lineSections);
}
