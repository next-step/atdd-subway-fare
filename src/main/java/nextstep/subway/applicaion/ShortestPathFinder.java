package nextstep.subway.applicaion;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SearchType;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ShortestPathFinder {

    Path findPath(final List<Line> lines, final Station source, final Station target, final SearchType searchType);
}
