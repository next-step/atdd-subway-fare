package nextstep.subway.path.domain.policy;

public class Over50KmPolicy implements FarePolicy {


  public static final int MIN_DISTANCE = 8;
  public static final int POLICY_DISTANCE = 8;
  private FarePolicy nextFarePolicy;

  @Override
  public void setNextPolicy(FarePolicy farePolicy) {
    this.nextFarePolicy = farePolicy;
  }

  @Override
  public long calculate(int totalDistance,long price) {
    long additionalPriceOver50 = 0;
    int remainDistance = getRemainDistance(totalDistance);
    if (remainDistance >= MIN_DISTANCE) {
      additionalPriceOver50 = calculateOverFare(remainDistance, POLICY_DISTANCE);
    }
    return price + additionalPriceOver50;
  }

  private int getRemainDistance(int totalDistance) {
    return totalDistance - 50;
  }
}
