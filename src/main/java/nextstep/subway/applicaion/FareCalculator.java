package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.subway.domain.fare.AgeBasedDiscountApplier;
import nextstep.subway.domain.fare.DistanceBasedSurchargeApplier;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FareApplierChain;
import nextstep.subway.domain.fare.LineBasedSurchargeApplier;
import nextstep.subway.domain.vo.Path;

public class FareCalculator {

  public Fare calculate(final Path path, final Member member) {
    final var fare = Fare.baseFare();

    FareApplierChain chain = new LineBasedSurchargeApplier(path.getLines())
        .apply(new DistanceBasedSurchargeApplier(path.getDistance()))
        .apply(new AgeBasedDiscountApplier(member.getAge()));

    return chain.calculate(fare);
  }
}
