package nextstep.subway.path.domain.policy;

public class BasicFarePolicy implements FarePolicy {

  private static final int DEFAULT_COST = 1250;
  private FarePolicy farePolicy;

  @Override
  public void setNextPolicy(FarePolicy farePolicy) {
    this.farePolicy = farePolicy;
  }

  @Override
  public long calculate(int remainDistance, long price) {
    if (remainDistance < 10) {
      return DEFAULT_COST;
    }
    return farePolicy.calculate(remainDistance, DEFAULT_COST);
  }

}
