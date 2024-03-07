package nextstep.subway.domain;

public abstract class FareApplier {

  public abstract Fare calculate(Fare fare);

  public FareApplierChain apply(FareApplier calculator) {
    return new FareApplierChain(this, calculator);
  }
}
