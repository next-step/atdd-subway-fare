package nextstep.subway.dto.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private Integer distance;
    private Integer duration;

    protected PathEdge() {}

    public PathEdge(Integer distance, Integer duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
