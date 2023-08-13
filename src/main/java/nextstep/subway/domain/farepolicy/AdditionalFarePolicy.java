package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.farepolicy.type.PolicyType;

public interface AdditionalFarePolicy {
  boolean isSatisfied(int value);
  int calculateFare(int criteria);
  PolicyType getPolicyType();
}
