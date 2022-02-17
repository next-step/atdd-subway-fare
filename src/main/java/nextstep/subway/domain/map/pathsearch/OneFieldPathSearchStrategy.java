package nextstep.subway.domain.map.pathsearch;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.graphfactory.OneFieldWeightGraphFactory;

public final class OneFieldPathSearchStrategy implements PathSearchStrategy {
    private final OneFieldWeightGraphFactory factory;

    public OneFieldPathSearchStrategy(OneFieldWeightGraphFactory factory) {
        this.factory = factory;
    }

    @Override
    public Path find(SubwayMap subwayMap, List<Line> lines, Station upStation, Station downStation) {
        return subwayMap.findPath(factory.createGraph(lines), upStation, downStation);
    }
}
