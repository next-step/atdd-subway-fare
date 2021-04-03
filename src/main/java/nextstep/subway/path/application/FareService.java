package nextstep.subway.path.application;

import java.util.Objects;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.springframework.stereotype.Service;

@Service
public class FareService {

  public Fare calculate(PathResult pathResult, LoginMember loginMember) {
    Fare fare = new Fare(pathResult.getTotalDistance(), pathResult.getMaxAdditionalFare());
    if (!Objects.isNull(loginMember)) {
      fare.applyMemberFarePolicy(loginMember);
    }
    return fare;
  }
}
