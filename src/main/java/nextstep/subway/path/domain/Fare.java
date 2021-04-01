package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.BasicFarePolicy;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.Over50KmPolicy;
import nextstep.subway.path.domain.policy.Over10KmPolicy;

public class Fare {

  private long cost;


  public Fare(int totalDistance) {
    if (totalDistance > 0) {
      this.cost = calculate(totalDistance);
    }
  }

  public long getCost() {
    return this.cost;
  }

  private FarePolicy setPolicy(){
    FarePolicy farePolicy = new BasicFarePolicy();
    Over10KmPolicy over10KmPolicy = new Over10KmPolicy();
    Over50KmPolicy over50KmPolicy = new Over50KmPolicy();
    farePolicy.setNextPolicy(over10KmPolicy);
    over10KmPolicy.setNextPolicy(over50KmPolicy);
    return farePolicy;
  }

  private long calculate(int totalDistance) {
    FarePolicy farePolicy = setPolicy();
    return farePolicy.calculate(totalDistance,0);
  }

}
