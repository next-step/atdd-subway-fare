package nextstep.subway.path.domain.policy;

public interface MemberFarePolicy {
  void setNextPolicy(MemberFarePolicy memberFarePolicy);
  long calculate(long price);
}
