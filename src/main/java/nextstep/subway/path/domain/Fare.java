package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.policy.AgePolicy;
import nextstep.subway.path.domain.policy.BasicFarePolicy;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.MemberFarePolicy;
import nextstep.subway.path.domain.policy.Over10KmPolicy;
import nextstep.subway.path.domain.policy.Over50KmPolicy;

public class Fare {

  private FarePolicy farePolicy;
  private long cost;

  public Fare(int totalDistance,int lineAdditionalFee) {
    if (totalDistance > 0) {
      farePolicy = setPolicy(lineAdditionalFee);
      this.cost = calculate(totalDistance,lineAdditionalFee);
    }
  }

  public long getCost() {
    return this.cost;
  }

  public void applyMemberFarePolicy(LoginMember loginMember) {
    MemberFarePolicy memberFarePolicy = setMemberPolicy(loginMember);
    this.cost = memberFarePolicy.calculate(cost);
  }

  private MemberFarePolicy setMemberPolicy(LoginMember loginMember){
    MemberFarePolicy memberFarePolicy =  new AgePolicy(loginMember.getAge());
    return memberFarePolicy;
  }

  private FarePolicy setPolicy(int lineAdditionalFee){
    FarePolicy farePolicy = new BasicFarePolicy(lineAdditionalFee);
    Over10KmPolicy over10KmPolicy = new Over10KmPolicy();
    Over50KmPolicy over50KmPolicy = new Over50KmPolicy();
    farePolicy.setNextPolicy(over10KmPolicy);
    over10KmPolicy.setNextPolicy(over50KmPolicy);
    return farePolicy;
  }

  private long calculate(int totalDistance,int lineAdditionalFee) {
    return farePolicy.calculate(totalDistance,0);
  }

}
