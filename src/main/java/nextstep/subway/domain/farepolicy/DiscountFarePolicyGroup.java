package nextstep.subway.domain.farepolicy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.domain.farepolicy.type.PolicyType;

public class DiscountFarePolicyGroup {
  public static final int ZERO = 0;
  public static final int CHILDREN_DISCOUNT_PERCENTAGE = 50;
  public static final int INITIAL_AGE_DISCOUNT = 350;
  public static final int YMCA_AGE_DISCOUNT_PERCENTAGE = 20;
  public static final int YMCA_UPPER_THRESHOLD = 19;
  public static final int YMCA_LOWER_THRESHOLD = 13;
  public static final int CHILDEREN_UPPER_THRESHOLD = 13;
  public static final int CHILDEREN_LOWER_THRESHOLD = 6;
  private final List<DiscountFarePolicy> discountPolicyGroup = new ArrayList<>();

  private DiscountFarePolicyGroup() {
    DiscountFarePolicy OverThirteenYearsOldPolicy =
        new AgeDiscountFarePolicy(YMCA_UPPER_THRESHOLD, YMCA_LOWER_THRESHOLD,
            YMCA_AGE_DISCOUNT_PERCENTAGE, INITIAL_AGE_DISCOUNT);
    DiscountFarePolicy OverSixYearsOldPolicy =
        new AgeDiscountFarePolicy(CHILDEREN_UPPER_THRESHOLD, CHILDEREN_LOWER_THRESHOLD,
            CHILDREN_DISCOUNT_PERCENTAGE, INITIAL_AGE_DISCOUNT);

    addDiscountPolicy(OverSixYearsOldPolicy);
    addDiscountPolicy(OverThirteenYearsOldPolicy);
  }

  public static DiscountFarePolicyGroup of(){
    return new DiscountFarePolicyGroup();
  }

  public int calculateFare(int fare, int age) {

    for (DiscountFarePolicy discountFarePolicy : discountPolicyGroup) {
      if (discountFarePolicy.getPolicyType().equals(PolicyType.DISCOUNT_AGE)
          && discountFarePolicy.isSatisfied(age)) {
        fare = discountFarePolicy.calculateFare(fare);
      }
    }

    return fare;
  }
  private void addDiscountPolicy(DiscountFarePolicy discountFarePolicy) {
    discountPolicyGroup.add(discountFarePolicy);
  }
}
