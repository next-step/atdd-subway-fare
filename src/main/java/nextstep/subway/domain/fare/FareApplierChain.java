package nextstep.subway.domain.fare;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FareApplierChain extends FareApplier {

  private FareApplier calculator;

  private FareApplier nextCalculator;

  @Override
  public Fare calculate(Fare fare) {
    return nextCalculator.calculate(
        calculator.calculate(fare)
    );
  }
}
