package nextstep.subway.domain.farepolicy;

import java.util.function.Predicate;
import lombok.Getter;
import nextstep.subway.domain.farepolicy.type.PolicyType;

public class AgeDiscountFarePolicy implements DiscountFarePolicy{

  private final int amount;
  @Getter
  public final PolicyType policyType;
  private final int percentage;
  private Predicate<Integer> lowerPredicate;
  private Predicate<Integer> upperPredicate;
  public AgeDiscountFarePolicy(int upperThreshold, int lowerThreshold, int percentage, int amount) {

    this.percentage = percentage;
    this.policyType = PolicyType.DISCOUNT_AGE;
    this.amount = amount;

    lowerPredicate = (integer) -> integer.compareTo(lowerThreshold) >= 0;
    upperPredicate = (integer) -> integer.compareTo(upperThreshold) < 0;
  }

  public boolean isSatisfied(int age){
    return upperPredicate.test(age) && lowerPredicate.test(age);
  }

  @Override
  public int calculateFare(int fare) {
    return (int) ((fare - 350) * (100 - percentage) * 0.01);
  }
}
