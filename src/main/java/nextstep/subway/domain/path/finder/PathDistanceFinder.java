package nextstep.subway.domain.path.finder;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.PathType;

public class PathDistanceFinder extends PathFinderImpl {

  public PathDistanceFinder(List<Line> lines) {
    super(lines);
  }
}
