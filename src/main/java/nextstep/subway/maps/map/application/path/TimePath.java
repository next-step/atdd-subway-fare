package nextstep.subway.maps.map.application.path;

import java.time.LocalDateTime;

import nextstep.subway.maps.map.domain.SubwayPath;

public class TimePath {

    private final SubwayPath path;

    public TimePath(SubwayPath path) {
        this.path = path;
    }

    public SubwayPath getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        return departTime;
    }
}
