package nextstep.subway.domain.farepolicy;

import java.util.function.Predicate;
import lombok.Getter;
import nextstep.subway.domain.farepolicy.type.PolicyType;

public class DistanceFarePolicy implements AdditionalFarePolicy{

  public static final int ZERO = 0;
  @Getter
  public PolicyType policyType;
  private final int upperThreshold;
  private final int lowerThreshold;
  private int extraFare;
  private int perKilometer;
  private static int MINIMUM = 0;
  private Predicate<Integer> lowerPredicate;
  private Predicate<Integer> upperPredicate;

  public DistanceFarePolicy(int upperThreshold, int lowerThreshold, int extraFare, int perKilometer) {
    this.upperThreshold = upperThreshold;
    this.lowerThreshold = lowerThreshold;
    this.extraFare = extraFare;
    this.perKilometer = perKilometer;
    this.policyType = PolicyType.DISTANCE;

    lowerPredicate = (integer) -> integer.compareTo(lowerThreshold) > 0;
  }
  @Override
  public boolean isSatisfied(int distance){
    return lowerPredicate.test(distance);
  }

  @Override
  public int calculateFare(int distance) {
    return calculateOverFare(extractOverDistance(distance));
  }

  private int extractOverDistance(int distance){
    if (distance >= upperThreshold){
      return upperThreshold - lowerThreshold;
    }
    return Math.max(distance - lowerThreshold, ZERO);
  }

  private int calculateOverFare(int distance) {
    if (distance == MINIMUM) {
      return MINIMUM;
    }
    return (int) ((Math.ceil((distance - 1) / perKilometer) + 1) * extraFare);
  }
}
