package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.subway.applicaion.dto.FindPathResponse;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.ui.BusinessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {

  private final SectionService sectionService;
  private final StationService stationService;
  private final MemberService memberService;
  private final FareCalculator fareCalculator = new FareCalculator();

  public FindPathResponse findPath(
      final Long source,
      final Long target,
      final PathSearchType type,
      final String memberEmail

  ) {
    verifySourceIsSameToTarget(source, target);

    final var sourceStation = stationService.getStation(source)
        .orElseThrow(() -> new BusinessException("출발역 정보를 찾을 수 없습니다."));
    final var targetStation = stationService.getStation(target)
        .orElseThrow(() -> new BusinessException("도착역 정보를 찾을 수 없습니다."));

    final var sections = sectionService.findAll();
    final var path = PathFinderComposite.find(sections, type, sourceStation, targetStation);

    final var member = memberService.findMemberByEmail(memberEmail)
        .orElseThrow(() -> new AuthenticationException("유효한 인증 토큰이 아닙니다."));

    final var fare = fareCalculator.calculate(path, member);

    return new FindPathResponse(path.getVertices(), path.getDistance(), path.getDuration(), fare.getTotalFare());
  }

  private void verifySourceIsSameToTarget(Long source, Long target) {
    if (source.equals(target)) {
      throw new BusinessException("출발역과 도착역이 같습니다.");
    }
  }
}
