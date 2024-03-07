package nextstep.subway.domain.fare;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AgeBasedDiscountApplier extends FareApplier {

  private final int age;

  @Override
  public Fare calculate(Fare fare) {
    fare.applyDiscount(
        AgeBasedDiscountPolicy.getApplicablePolicy(age).calculate(fare.getTotalFare(), age)
    );

    return fare;
  }
}
