package nextstep.core.subway.pathFinder.application.dto;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathCompositeWeightEdge extends DefaultWeightedEdge {

    private final int distance;
    private final int duration;

    public PathCompositeWeightEdge(int distance, int duration) {
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
