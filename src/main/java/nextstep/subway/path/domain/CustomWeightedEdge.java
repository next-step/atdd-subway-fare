package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightedEdge extends DefaultWeightedEdge {
    private final int distance;
    private final int duration;

    public CustomWeightedEdge(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
