package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.vo.Path;

@Component
public class FareCalculator {

  public Fare calculate(final Path path, final Member member) {
    return Fare.baseFare();
  }

  public int calculate(final int distance) {
    return basicCalculator.calculate()
        + over10kmSurchargeCalculator.calculate(distance)
        + over50kmSurchargeCalculator.calculate(distance);
  }
}
