package nextstep.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathWeightEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    protected PathWeightEdge() {}

    public PathWeightEdge(int distance, int duration) {
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
