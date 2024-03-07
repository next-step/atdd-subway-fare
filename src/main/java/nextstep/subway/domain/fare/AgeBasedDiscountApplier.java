package nextstep.subway.domain.fare;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AgeBasedDiscountApplier extends FareApplier {

  private final int age;

  @Override
  public Fare calculate(Fare fare) {
    AgeBasedDiscountPolicy.getApplicablePolicy(age)
        .ifPresent(policy -> fare.applyDiscount(policy.calculate(fare.getTotalFare(), age)));

    return fare;
  }
}
