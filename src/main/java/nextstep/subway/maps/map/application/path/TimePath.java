package nextstep.subway.maps.map.application.path;

import java.time.LocalDateTime;

import org.jgrapht.GraphPath;

import nextstep.subway.maps.map.domain.LineStationEdge;

public class TimePath {

    private final GraphPath<Long, LineStationEdge> path;

    public TimePath(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public GraphPath<Long, LineStationEdge> getPath() {
        return path;
    }

    public LocalDateTime getArrivalTime(LocalDateTime departTime) {
        return LocalDateTime.now();
    }
}
