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

    public void setDistance(final int distance) {
        this.distance = distance;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }
}
