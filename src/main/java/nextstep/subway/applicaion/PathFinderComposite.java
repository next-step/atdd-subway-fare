package nextstep.subway.applicaion;

import java.util.Collection;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.vo.Path;
import nextstep.subway.ui.BusinessException;

public class PathFinderComposite {

  private static PathFinder getPathFinder(final Collection<Section> sections, final PathSearchType type) {
    if (PathSearchType.DISTANCE.equals(type)) {
      return new ShortestDistancePathFinder(sections);
    } else {
      return new ShortestDurationPathFinder(sections);
    }
  }

  public static Path find(
      final Collection<Section> sections,
      final PathSearchType type,
      final Station source,
      final Station target
  ) {
    final var pathFinder = getPathFinder(sections, type);
    return pathFinder.find(source, target)
        .orElseThrow(() -> new BusinessException("경로를 찾을 수 없습니다."));
  }
}
