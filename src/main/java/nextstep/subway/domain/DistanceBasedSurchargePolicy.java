package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DistanceBasedSurchargePolicy implements FarePolicy {

  // 10km 초과 50km 이하 초과거리에 대한 추가요금 정책
  TEN_KILOMETER_SURCHARGE_POLICY(10, 50, 100, 5),

  // 50km 초과거리에 대한 추가요금 정책
  FIFTY_KILOMETER_SURCHARGE_POLICY(TEN_KILOMETER_SURCHARGE_POLICY.distanceUpper, Integer.MAX_VALUE, 100, 8),
  ;

  private final int distanceUnder;
  private final int distanceUpper;
  private final int surchargePerUnitDistance;
  private final int unitDistance;
  private boolean isApplicable(final int distance) {
    return distanceUnder < distance;
  }

  // 적용 구간에 대한 추가요금을 반환
  public int calculate(final int distance) {
    if (!isApplicable(distance)) {
      throw new IllegalArgumentException("추가요금 적용 대상이 아닙니다.");
    }

    final int applicableDistance = Math.min(distance, distanceUpper) - distanceUnder;
    return (((applicableDistance - 1) / unitDistance) + 1) * surchargePerUnitDistance;
  }

  public static List<DistanceBasedSurchargePolicy> getApplicablePolicies(final int distance) {
    return Arrays.stream(DistanceBasedSurchargePolicy.values())
        .filter(it -> it.isApplicable(distance))
        .collect(Collectors.toList());
  }
}
