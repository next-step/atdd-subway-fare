package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.finder.PathDistanceFinder;
import nextstep.subway.domain.path.finder.PathDurationFinder;
import nextstep.subway.domain.path.finder.PathFinder;

public enum PathType {

  DISTANCE("거리"),
  DURATION("시간");

  private String description;

  PathType(String description) {
    this.description = description;
  }

  public PathFinder getPathFinder(List<Line> lines) {
    if (this == DISTANCE) {
      return new PathDistanceFinder(lines);
    }

    return new PathDurationFinder(lines);
  }
}
