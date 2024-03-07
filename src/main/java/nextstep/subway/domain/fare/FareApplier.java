package nextstep.subway.domain.fare;

public abstract class FareApplier {

  public abstract Fare calculate(Fare fare);

  public FareApplierChain apply(FareApplier calculator) {
    return new FareApplierChain(this, calculator);
  }
}
