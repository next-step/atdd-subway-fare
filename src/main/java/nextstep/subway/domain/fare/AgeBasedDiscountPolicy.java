package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgeBasedDiscountPolicy implements FarePolicy {

  // 유아
  INFANT_DISCOUNT_POLICY(0, 6, 100, 0),

  // 6세 이상 13세 미만 어린이 할인 정책
  CHILD_DISCOUNT_POLICY(INFANT_DISCOUNT_POLICY.ageUpper, 13, 50, 350),

  // 13세 이상 19세 미만 청소년 할인 정책
  JUVENILE_DISCOUNT_POLICY(CHILD_DISCOUNT_POLICY.ageUpper, 19, 20, 350),

  // 성인 할인 정책
  ADULT_DISCOUNT_POLICY(JUVENILE_DISCOUNT_POLICY.ageUpper, Integer.MAX_VALUE, 0, 0),
  ;

  private final int ageUnder;
  private final int ageUpper;
  private final double discountRate;
  private final double deductAmount;

  private boolean isApplicable(final int age) {
    return ageUnder <= age && age < ageUpper;
  }

  //  할인 금액을 반환
  public int calculate(final int totalFare, final int age) {
    if (!isApplicable(age)) {
      throw new IllegalArgumentException("할인 적용 대상이 아닙니다.");
    }

    return (int) ((totalFare - deductAmount) * discountRate / 100);
  }

  public static AgeBasedDiscountPolicy getApplicablePolicy(final int age) {
    return Arrays.stream(AgeBasedDiscountPolicy.values())
        .filter(it -> it.isApplicable(age))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("적용할 수 있는 할인 정책이 없습니다."));
  }
}
