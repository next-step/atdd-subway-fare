package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.FindPathResponse;
import nextstep.subway.domain.BaseFareCalculator;
import nextstep.subway.domain.Over10kmSurchargeCalculator;
import nextstep.subway.domain.Over50kmSurchargeCalculator;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.domain.vo.Path;
import nextstep.subway.ui.BusinessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {

  private final SectionService sectionService;
  private final StationService stationService;

  public FindPathResponse findPath(
      final Long source,
      final Long target,
      final PathSearchType type
  ) {
    verifySourceIsSameToTarget(source, target);

    final var sourceStation = stationService.getStation(source)
        .orElseThrow(() -> new BusinessException("출발역 정보를 찾을 수 없습니다."));
    final var targetStation = stationService.getStation(target)
        .orElseThrow(() -> new BusinessException("도착역 정보를 찾을 수 없습니다."));

    final var sections = sectionService.findAll();
    final var path = PathFinderComposite.find(sections, type, sourceStation, targetStation);

    final var fare = calculateFare(path);

    return new FindPathResponse(path.getVertices(), path.getDistance(), path.getDuration(), fare);
  }

  private int calculateFare(Path path) {
    final var basicCalculator = new BaseFareCalculator();
    final var over10kmSurchargeCalculator = new Over10kmSurchargeCalculator();
    final var over50kmSurchargeCalculator = new Over50kmSurchargeCalculator();

    return basicCalculator.calculate()
        + over10kmSurchargeCalculator.calculate(path.getDistance())
        + over50kmSurchargeCalculator.calculate(path.getDistance());
  }

  private void verifySourceIsSameToTarget(Long source, Long target) {
    if (source.equals(target)) {
      throw new BusinessException("출발역과 도착역이 같습니다.");
    }
  }
}
