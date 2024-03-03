package nextstep.subway.domain;

/**
 * 50km 이상 거리에 대한 추가 요금을 계산한다.
 * 추가 요금은 8km 당 100원이다.
 */
public class Over50kmSurchargeCalculator {

  private final int DISTANCE_UNDER = 50;
  private final int SURCHARGE_PER_UNIT_DISTANCE = 100;
  private final int UNIT_DISTANCE = 8;

  private boolean isApplicable(final int distance) {
    return DISTANCE_UNDER < distance;
  }

  public int calculate(final int distance) {
    if (!isApplicable(distance)) {
      return 0;
    }

    final int applicableDistance = distance - DISTANCE_UNDER;
    return (int) ((Math.ceil((applicableDistance - 1) / UNIT_DISTANCE) + 1) * SURCHARGE_PER_UNIT_DISTANCE);
  }
}
