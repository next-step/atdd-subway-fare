package nextstep.subway.path.domain.policy;

public class BasicFarePolicy implements FarePolicy {

  private static final int DEFAULT_COST = 1250;
  private int lineAdditionalFee = 0;
  private FarePolicy nextFarePolicy;

  public BasicFarePolicy(int lineAdditionalFee) {
    this.lineAdditionalFee = lineAdditionalFee;
  }

  @Override
  public void setNextPolicy(FarePolicy farePolicy) {
    this.nextFarePolicy = farePolicy;
  }

  @Override
  public long calculate(int remainDistance, long price) {
    if (remainDistance < 10) {
      return DEFAULT_COST + lineAdditionalFee;
    }
    return nextFarePolicy.calculate(remainDistance, DEFAULT_COST+lineAdditionalFee);
  }

}
