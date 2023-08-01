package subway.path.application;

import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    public PathRetrieveResponse findPath(List<Section> sections, Station sourceStation, Station targetStation);
}
