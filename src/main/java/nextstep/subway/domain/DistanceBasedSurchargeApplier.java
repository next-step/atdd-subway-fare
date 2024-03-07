package nextstep.subway.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DistanceBasedSurchargeApplier extends FareCalculator {

  private final int distance;

  @Override
  public Fare calculate(final Fare fare) {
    DistanceBasedSurchargePolicy.getApplicablePolicies(distance).forEach(
        policy -> fare.addSurcharge(policy.calculate(distance))
    );

    return fare;
  }
}
