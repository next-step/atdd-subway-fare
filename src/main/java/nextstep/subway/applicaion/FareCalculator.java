package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.subway.domain.AgeBasedDiscountPolicy;
import nextstep.subway.domain.DistanceBasedSurchargePolicy;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.vo.Path;

public class FareCalculator {

  public Fare calculate(final Path path, final Member member) {
    final var fare = Fare.baseFare();
    fare.addSurcharge(getDistanceSurcharge(path.getDistance()));
    fare.applyDiscount(getDiscountAmount(fare.getTotalFare(), member.getAge()));

    return fare;
  }

  private int getDistanceSurcharge(final int distance) {
    return DistanceBasedSurchargePolicy.getApplicablePolicies(distance).stream()
        .mapToInt(it -> it.calculate(distance))
        .sum();
  }

  private int getDiscountAmount(final int totalFare, final int age) {
    return AgeBasedDiscountPolicy.getApplicablePolicy(age)
        .map(discountPolicy -> discountPolicy.calculate(totalFare, age))
        .orElse(0);
  }
}
