package nextstep.subway.domain.path.finder;

public class FareCalculator {

  public static int calculator(int distance) {
    if (distance <= 10) {
      return FareCalculatorRule.getTenBelowFare();
    }

    return distance <= 50 ? FareCalculatorRule.getfiftyBelowFare(distance) : FareCalculatorRule.getfiftyExcessFare(distance);
  }
}
