package nextstep.subway.applicaion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@AllArgsConstructor
public class PathWeightedEdge extends DefaultWeightedEdge {

  private final int distance;
  private final int duration;
}
