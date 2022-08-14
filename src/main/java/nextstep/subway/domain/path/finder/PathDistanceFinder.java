package nextstep.subway.domain.path.finder;

import java.util.List;
import nextstep.subway.domain.Line;

public class PathDistanceFinder extends PathFinderImpl {

  public PathDistanceFinder(List<Line> lines) {
    super(lines);
  }
}
