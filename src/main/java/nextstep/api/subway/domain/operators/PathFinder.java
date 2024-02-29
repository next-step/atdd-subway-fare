package nextstep.api.subway.domain.operators;

import java.util.List;

import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.model.vo.Path;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
public interface PathFinder {

	Path findShortestPathBySections(Station sourceStation, Station targetStation, List<Section> sections);
	Path findShortestPathByLines(Station sourceStation, Station targetStation, List<Line> lines);

}
