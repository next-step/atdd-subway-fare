package nextstep.subway.domain.path.finder;

public class FareCalculator {

  private static final int BASIC_FARE = 1250;
  private static final int EXTRA_FARE = 100;
  private static final int STANDARD_FIFTY = 51;
  private static final int DEDUCTION = 350;

  public int calculator(int distance, int age, int maxLineFare) {
    int fare = getCalculatorFare(distance);
    return byAgeCalculateFare(age, fare + maxLineFare);
  }

  private int getCalculatorFare(int distance) {
    if (distance <= 10) {
      return getTenBelowFare();
    }

    return distance <= 50 ? getfiftyBelowFare(distance) : getfiftyExcessFare(distance);
  }

  private int getTenBelowFare() {
    return BASIC_FARE;
  }

  private int getfiftyBelowFare(int distance) {
    return (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * EXTRA_FARE) + BASIC_FARE;
  }

  private int getfiftyExcessFare(int distance) {
    return (int) ((Math.ceil((distance - STANDARD_FIFTY) / 8)) * EXTRA_FARE) + getfiftyBelowFare(STANDARD_FIFTY);
  }

  private int byAgeCalculateFare(int age, int fare) {
    if (isChildCheck(age)) {
      return (int) ((fare - DEDUCTION) *0.5);
    }

    if (isTeenCheck(age)) {
      return (int) ((fare - DEDUCTION) * 0.8);
    }

    return fare;
  }

  private boolean isChildCheck(int age) {
    return age >= 6 && age < 13;
  }

  private boolean isTeenCheck(int age) {
    return age >= 13 && age < 19;
  }
}
