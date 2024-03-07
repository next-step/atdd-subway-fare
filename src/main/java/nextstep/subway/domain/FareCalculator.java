package nextstep.subway.domain;

public abstract class FareCalculator {

  public abstract Fare calculate(Fare fare);

  public FareCalculatorChain apply(FareCalculator calculator) {
    return new FareCalculatorChain(this, calculator);
  }
}
