package nextstep.subway.domain;

import nextstep.subway.application.dto.PathResponse;

import java.util.List;

public interface PathFinder {
    boolean isType(PathType pathType);
    PathResponse findPath(final List<Section> sections, final Station sourceStation, final Station targetStation);
}
