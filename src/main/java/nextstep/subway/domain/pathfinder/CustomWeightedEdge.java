package nextstep.subway.domain.pathfinder;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class CustomWeightedEdge extends DefaultWeightedEdge {
    private Long distance;
    private Long duration;

    public void addDistance(Long distance) {
        this.distance = distance;
    }

    public void addDuration(Long duration) {
        this.duration = duration;
    }
}
