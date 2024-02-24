package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomWeightedEdge extends DefaultWeightedEdge {
    private double distance;
    private int duration;

    public CustomWeightedEdge() {
    }

    public double getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }
}
