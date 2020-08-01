package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.TimePath;
import org.jgrapht.GraphPath;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TimePaths {

    private final List<TimePath> paths;

    private TimePaths(List<GraphPath<Long, LineStationEdge>> paths) {
        this.paths = paths.stream()
                .map(TimePath::new)
                .collect(Collectors.toList());
    }

    public static TimePaths of(List<GraphPath<Long, LineStationEdge>> paths) {
        return new TimePaths(paths);
    }

    public TimePath findFastestArrivalPath(LocalDateTime departTime) {
        return null;
    }
}
