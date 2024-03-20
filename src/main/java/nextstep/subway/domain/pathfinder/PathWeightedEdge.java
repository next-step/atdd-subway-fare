package nextstep.subway.domain.pathfinder;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class PathWeightedEdge extends DefaultWeightedEdge {
    private Long distance;
    private Long duration;
    private Long additionalFare;

    public void addDistance(Long distance) {
        this.distance = distance;
    }

    public void addDuration(Long duration) {
        this.duration = duration;
    }

    public void addAdditionalFare(Long additionalFare) {
        this.additionalFare = additionalFare;
    }
}
