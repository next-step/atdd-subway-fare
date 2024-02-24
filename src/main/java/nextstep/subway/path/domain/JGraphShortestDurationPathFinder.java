package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class JGraphShortestDurationPathFinder implements PathFinder {
    @Override
    public Path shortcut(Lines lines,
                         Station source,
                         Station target) {
        return null;
    }

    @Override
    public GraphPath validCorrect(Lines lines,
                                  Station source,
                                  Station target) {
        return null;
    }
}
