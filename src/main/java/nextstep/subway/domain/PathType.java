package nextstep.subway.domain;

import java.util.List;

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
