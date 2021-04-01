package nextstep.subway.path.domain.policy;

public class Over10KmPolicy implements FarePolicy {

  public static final int MIN_DISTANCE = 5;
  public static final int POLICY_DISTANCE = 5;
  public static final int OVER_10KM_MAX_FARE = 800;
  private FarePolicy farePolicy;

  @Override
  public void setNextPolicy(FarePolicy farePolicy) {
    this.farePolicy = farePolicy;
  }

  @Override
  public long calculate(int totalDistance, long price) {
    long additionalPriceOver10 = 0;
    int remainDistance = getRemainDistance(totalDistance);
    if (remainDistance >= MIN_DISTANCE) {
      additionalPriceOver10 = Math
          .min(calculateOverFare(remainDistance, POLICY_DISTANCE), OVER_10KM_MAX_FARE);
    }
    return farePolicy.calculate(totalDistance,price + additionalPriceOver10);
  }

  private int getRemainDistance(int totalDistance) {
    return totalDistance - 10;
  }
}
