package nextstep.subway.domain;

public class BaseFareCalculator {

  // 기본요금은 1,250원
  private final int BASE_FARE = 1250;

  public int calculate() {
    return BASE_FARE;
  }
}
