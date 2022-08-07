package nextstep.subway.domain.subwaymap;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;

public interface SubwayMap {

    Path findPath(Station source, Station target);

}
