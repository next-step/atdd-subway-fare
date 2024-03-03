package nextstep.path;

import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@Setter
public class DistanceDurationEdge extends DefaultWeightedEdge {
    private int distance;
    private int duration;
}
