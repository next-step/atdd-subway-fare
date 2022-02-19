package nextstep.subway.domain.map;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import nextstep.subway.domain.map.Path;

public class Paths {
    private final List<Path> paths;

    public Paths(List<Path> paths) {
        this.paths = paths;
    }

    public Path fastestPathForArrivalTime(LocalDateTime time) {
        paths.forEach(eachPath -> eachPath.applyArrivalTime(time));
        return paths.stream()
                    .max(Comparator.comparing(Path::getArrivalTime))
                    .orElseThrow(IllegalArgumentException::new);
    }

    public int size() {
        return paths.size();
    }
}
