package nextstep.subway.path.domain.policy;

public class AgePolicy implements MemberFarePolicy {

  private int age;
  private MemberFarePolicy memberFarePolicy;

  public AgePolicy(int age) {
    this.age = age;
  }

  @Override
  public void setNextPolicy(MemberFarePolicy memberFarePolicy) {
    this.memberFarePolicy = memberFarePolicy;
  }

  @Override
  public long calculate(long price) {
    if (isYouth()) {
      return calculateYouthFare(price);
    }
    else if (isChildren()) {
      return calculateChildFare(price);
    }
    return price;
  }

  private boolean isYouth(){
    return age >= 13 && age < 19;
  }
  private boolean isChildren(){
    return age >= 6 && age < 13;
  }

  private long calculateYouthFare(long price) {
    return (long) (((price - 350) * 0.8) + 350);
  }

  private long calculateChildFare(long price) {
    return (long) (((price - 350) * 0.5) + 350);
  }
}
