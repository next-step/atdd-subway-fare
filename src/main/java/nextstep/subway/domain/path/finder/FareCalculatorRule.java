package nextstep.subway.domain.path.finder;

public class FareCalculatorRule {

  private static final int BASICFARE = 1250;
  private static final int EXTRAFARE = 100;
  private static final int STANDARDFIFTY = 51;
  private static final int ONE = 1;

  static int getTenBelowFare() {
    return BASICFARE;
  }

  static int getfiftyBelowFare(int distance) {
    return (int) ((Math.ceil(((distance - 10) - 1) / 5) + ONE) * EXTRAFARE) + BASICFARE;
  }

  static int getfiftyExcessFare(int distance) {
    return (int) ((Math.ceil((distance - STANDARDFIFTY) / 8)) * EXTRAFARE) + getfiftyBelowFare(STANDARDFIFTY);
  }
}
