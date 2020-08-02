package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.domain.TimePath;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimePaths {

    private final List<TimePath> paths;

    private TimePaths(List<SubwayPath> paths) {
        this.paths = paths.stream()
                .map(TimePath::new)
                .collect(Collectors.toList());
    }

    public static TimePaths of(List<SubwayPath> paths) {
        return new TimePaths(paths);
    }

    public TimePath findFastestArrivalPath(LocalDateTime departTime) {
        return this.paths.stream()
                .min(Comparator.comparing(timePath -> timePath.getArrivalTime(departTime)))
                .orElseThrow(() -> new RuntimeException("경로가 존재하지 않는 요청입니다."));
    }
}
