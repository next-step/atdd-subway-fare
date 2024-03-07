package nextstep.subway.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FareCalculatorChain extends FareCalculator {

  private FareCalculator calculator;

  private FareCalculator nextCalculator;

  @Override
  public Fare calculate(Fare fare) {
    return nextCalculator.calculate(
        calculator.calculate(fare)
    );
  }
}
