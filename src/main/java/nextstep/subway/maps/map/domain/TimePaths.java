package nextstep.subway.maps.map.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

public class TimePaths {

    private final List<SubwayPath> paths;

    private TimePaths(List<SubwayPath> paths) {
        this.paths = Collections.unmodifiableList(Lists.newArrayList(paths));
    }

    public static TimePaths of(List<SubwayPath> paths) {
        return new TimePaths(paths);
    }

    public SubwayPath findFastestArrivalPath(LocalDateTime departTime) {
        return this.paths.stream()
            .min(Comparator.comparing(timePath -> timePath.getArrivalTime(departTime)))
            .orElseThrow(() -> new IllegalArgumentException("no paths found by given request from user."));
    }
}
