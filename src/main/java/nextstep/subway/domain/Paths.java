package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class Paths {
    private final List<Path> paths;

    public Paths(List<Path> paths) {
        this.paths = paths;
    }

    public Path fastestPath(LocalDateTime time) {
        paths.forEach(eachPath -> eachPath.applyArrivalTime(time));
        return paths.stream()
                    .max(Comparator.comparing(Path::getArrivalTime))
                    .orElseThrow(IllegalArgumentException::new);
    }

    public int size() {
        return paths.size();
    }
}
