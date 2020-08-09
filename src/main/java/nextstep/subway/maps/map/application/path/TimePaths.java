package nextstep.subway.maps.map.application.path;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import nextstep.subway.maps.map.domain.LineStationEdge;

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
        return this.paths.stream()
            .min(Comparator.comparing(timePath -> timePath.getArrivalTime(departTime)))
            .orElseThrow(() -> new IllegalArgumentException("no paths found by given request from user."));
    }
}
