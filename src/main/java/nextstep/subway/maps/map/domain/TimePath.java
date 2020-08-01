package nextstep.subway.maps.map.domain;

import org.jgrapht.GraphPath;

import java.time.LocalDateTime;

public class TimePath {

    private final SubwayPath path;

    public TimePath(SubwayPath path) {
        this.path = path;
    }

    public SubwayPath getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        return LocalDateTime.now();
    }
}
