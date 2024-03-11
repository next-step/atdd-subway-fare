package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightedEdge extends DefaultWeightedEdge {
    private final long distance;
    private final long duration;

    public CustomWeightedEdge(long distance, long duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public long getDistance() {
        return distance;
    }

    public long getDuration() {
        return duration;
    }
}
