package nextstep.subway.domain;

import java.util.Arrays;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgeBasedDiscountPolicy implements FarePolicy {

  // 6세 이상 13세 미만 어린이 할인 정책
  CHILD_DISCOUNT_POLICY(6, 13, 50),

  // 13세 이상 19세 미만 청소년 할인 정책
  JUVENILE_DISCOUNT_POLICY(CHILD_DISCOUNT_POLICY.ageUpper, 19, 20),
  ;

  private final int ageUnder;
  private final int ageUpper;
  private final double discountRate;

  private boolean isApplicable(final int age) {
    return ageUnder <= age && age < ageUpper;
  }

  //  할인 금액을 반환
  public int calculate(final int totalFare, final int age) {
    if (!isApplicable(age)) {
      throw new IllegalArgumentException("추가요금 적용 대상이 아닙니다.");
    }

    return (int) ((totalFare - 350) * discountRate / 100);
  }

  public static Optional<AgeBasedDiscountPolicy> getApplicablePolicy(final int age) {
    return Arrays.stream(AgeBasedDiscountPolicy.values())
        .filter(it -> it.isApplicable(age))
        .findFirst();
  }
}
