package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.subway.domain.AgeBasedDiscountApplier;
import nextstep.subway.domain.DistanceBasedSurchargeApplier;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.FareCalculatorChain;
import nextstep.subway.domain.LineBasedSurchargeApplier;
import nextstep.subway.domain.vo.Path;

public class FareCalculator {

  public Fare calculate(final Path path, final Member member) {
    final var fare = Fare.baseFare();

    FareCalculatorChain chain = new LineBasedSurchargeApplier(path.getLines())
        .apply(new DistanceBasedSurchargeApplier(path.getDistance()))
        .apply(new AgeBasedDiscountApplier(member.getAge()));

    return chain.calculate(fare);
  }
}
