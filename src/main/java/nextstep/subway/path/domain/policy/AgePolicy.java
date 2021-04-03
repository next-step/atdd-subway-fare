package nextstep.subway.path.domain.policy;

public class AgePolicy implements MemberFarePolicy{

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
    if(age >=13 && age < 19) {
      return (long) (((price-350) * 0.8) + 350);
    }
    else if(age >= 6 && age < 13) {
      return (long) (((price-350) * 0.5) + 350);
    }
    return price;
  }

}
