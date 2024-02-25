package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightedEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    public CustomWeightedEdge() {
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void addDistance(final int distance) {
        this.distance = distance;
    }

    public void addDuration(final int duration) {
        this.duration = duration;
    }
}
