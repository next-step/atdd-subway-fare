package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.finder.PathDistanceFinder;
import nextstep.subway.domain.path.finder.PathFinder;

public class FareCalculator {

  private final List<Line> lines;
  private final Station source;
  private final Station target;

  public FareCalculator(List<Line> lines, Station source, Station target) {
    this.lines = lines;
    this.source = source;
    this.target = target;
  }

  public static int calculator(int distance) {
    if (distance <= 10) {
      return 1250;
    } else if (distance <= 50) {
      return (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100) + 1250;
    }

    return (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100) + (int) ((Math.ceil((41 - 1) / 5) + 1) * 100) + 1250;
  }
}
