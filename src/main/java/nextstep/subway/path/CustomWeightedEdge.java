package nextstep.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightedEdge extends DefaultWeightedEdge {
    private final long duration;
    private final long distance;

    public CustomWeightedEdge(long duration, long distance) {
        this.duration = duration;
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public long getDistance() {
        return distance;
    }
}
