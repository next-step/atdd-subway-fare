package nextstep.subway.maps.map.domain;

import org.jgrapht.GraphPath;

public class TimePath {

    private final GraphPath<Long, LineStationEdge> path;

    public TimePath(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public GraphPath<Long, LineStationEdge> getPath() {
        return path;
    }
}
