package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.BasicFarePolicy;
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

  private long calculate(int totalDistance) {
    BasicFarePolicy basicFarePolicy = new BasicFarePolicy();
    Over10KmPolicy over10KmPolicy = new Over10KmPolicy();
    Over50KmPolicy over50KmPolicy = new Over50KmPolicy();

    return basicFarePolicy.calculate(totalDistance) +
        over10KmPolicy.calculate(totalDistance) +
        over50KmPolicy.calculate(totalDistance);
  }

}
