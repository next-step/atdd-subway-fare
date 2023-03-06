package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Paths {
    private final List<Path> paths;

    public Paths(List<Path> paths) {
        this.paths = paths;
    }

    public Path getMiniumArrivalPath(LocalDateTime departureDate) {
        Path miniumArrivalPath = null;
        LocalDateTime miniumArrivalDate = LocalDateTime.MAX;
        for (Path path : paths) {
            LocalDateTime arrivalTime = path.getArrivalTime(departureDate);
            if (miniumArrivalDate.isBefore(arrivalTime)) {
                miniumArrivalDate = arrivalTime;
                miniumArrivalPath = path;
            }
        }
        return miniumArrivalPath;
    }
}
