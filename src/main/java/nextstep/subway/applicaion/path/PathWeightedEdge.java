package nextstep.subway.applicaion.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Line;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@AllArgsConstructor
public class PathWeightedEdge extends DefaultWeightedEdge {


  private final Line line;
  private final int distance;
  private final int duration;
}
