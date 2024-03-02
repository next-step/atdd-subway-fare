package nextstep.subway.domain;

/**
 * 10km 이상 거리에 대한 추가 요금을 계산한다.
 * 10km 로부터 5km 당 100원의 추가 요금을 최대 50km 까지 부과한다.
 */
public class Over10kmSurchargeCalculator {

  private final int DISTANCE_UNDER = 10;
  private final int DISTANCE_UPPER = 50;
  private final int SURCHARGE_PER_UNIT_DISTANCE = 100;
  private final int UNIT_DISTANCE = 5;

  private boolean isApplicable(final int distance) {
    return DISTANCE_UNDER < distance;
  }

  public int calculate(final int distance) {
    if (!isApplicable(distance)) {
      return 0;
    }

    final int applicableDistance = Math.min(distance, DISTANCE_UPPER) - DISTANCE_UNDER;
    return (int) ((Math.ceil((applicableDistance - 1) / UNIT_DISTANCE) + 1) * SURCHARGE_PER_UNIT_DISTANCE);
  }
}
