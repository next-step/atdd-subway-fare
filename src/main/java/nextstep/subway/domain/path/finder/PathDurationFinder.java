package nextstep.subway.domain.path.finder;

import java.util.List;
import nextstep.subway.domain.Line;

public class PathDurationFinder extends PathFinderImpl {

  public PathDurationFinder(List<Line> lines) {
    super(lines);
  }
}
