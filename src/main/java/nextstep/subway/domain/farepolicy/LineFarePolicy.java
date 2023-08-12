package nextstep.subway.domain.farepolicy;

import lombok.Getter;
import nextstep.subway.domain.farepolicy.type.PolicyType;

public class LineFarePolicy implements AdditionalFarePolicy{
  @Getter
  public PolicyType policyType;

  public LineFarePolicy() {
    this.policyType = PolicyType.ADDITIONAL_LINE;
  }

  @Override
  public boolean isSatisfied(int value) {
    return true;
  }

  @Override
  public int calculateFare(int additionalFare) {
    return additionalFare;
  }
}
