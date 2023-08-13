package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.farepolicy.type.PolicyType;

public interface DiscountFarePolicy {
  boolean isSatisfied(int value);
  int calculateFare(int fare);
  PolicyType getPolicyType();
}
