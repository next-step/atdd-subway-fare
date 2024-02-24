package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public interface PathFinder {
    Path shortcut(Lines lines,
                  Station source,
                  Station target,
                  PathType type);

    void validCorrect(Lines lines,
                           Station source,
                           Station target);
}
