package nextstep.core.subway.path.application.dto;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathCompositeWeightEdge extends DefaultWeightedEdge {

    private final int distance;
    private final int duration;
    private final int additionalFare;

    public PathCompositeWeightEdge(int distance, int duration, int additionalFare) {
        this.distance = distance;
        this.duration = duration;
        this.additionalFare = additionalFare;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
