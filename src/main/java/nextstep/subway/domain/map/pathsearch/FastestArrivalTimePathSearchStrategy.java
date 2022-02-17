package nextstep.subway.domain.map.pathsearch;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.graphfactory.OneFieldWeightGraphFactory;
import nextstep.subway.domain.map.graphfactory.SubwayMapGraphFactory;

public final class FastestArrivalTimePathSearchStrategy implements PathSearchStrategy {
    private static final SubwayMapGraphFactory DEFAULT_GRAPH_FACTORY = new OneFieldWeightGraphFactory(
        section -> (double) section.getDistance()
    );

    private final SubwayMapGraphFactory graphFactory;
    private final LocalTime takeTime;

    public FastestArrivalTimePathSearchStrategy(LocalTime takeTime, SubwayMapGraphFactory graphFactory) {
        this.takeTime = takeTime;
        this.graphFactory = graphFactory;
    }

    public FastestArrivalTimePathSearchStrategy(LocalTime takeTime) {
        this(takeTime, DEFAULT_GRAPH_FACTORY);
    }

    @Override
    public Path find(SubwayMap subwayMap, List<Line> lines, Station upStation, Station downStation) {
        List<Path> paths = subwayMap.findPaths(graphFactory.createGraph(lines), upStation, downStation);
        paths.forEach(eachPath -> eachPath.applyArrivalTime(takeTime));
        return paths.stream()
                    .max(Comparator.comparing(Path::getArrivalTime))
                    .orElseThrow(IllegalArgumentException::new);
    }
}
