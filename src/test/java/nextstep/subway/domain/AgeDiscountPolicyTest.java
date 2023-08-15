package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDiscountPolicyTest {
    private static final int BASE_FARE = 1250;
    private static final int CHILD_DISCOUNT_RATE = 50;
    private static final int TEEN_DISCOUNT_RATE = 20;


    // parameterized test로 어린이, 청소년, 어른을 한 번에 테스트 할 수도 있지만 정책적으로 분리하는 관점에서 테스트를 따로 작성함
    // 로직이 다 드러나 있는데 TDD를 위해 이 테스트를 작성할 필요가 있나 싶다
    @DisplayName("discountByAge : 어린이 요금 할인")
    @ParameterizedTest
    @ValueSource(ints = { 6, 12 })
    void extractFare_child(int age) {
        int expected = (int) Math.round(BASE_FARE * ((double) (100 - CHILD_DISCOUNT_RATE) / 100));
        assertThat(AgeDiscountPolicy.discountFromAge(age, BASE_FARE)).isEqualTo(expected);
    }

    @DisplayName("discountByAge : 청소년 사용자 요금")
    @ParameterizedTest
    @ValueSource(ints = { 13, 18 })
    void extractFare_teen(int age) {
        int expected = (int) Math.round(BASE_FARE * ((double) (100 - TEEN_DISCOUNT_RATE) / 100));
        assertThat(AgeDiscountPolicy.discountFromAge(age, BASE_FARE)).isEqualTo(expected);
    }

    @DisplayName("discountByAge : 일반 사용자 요금")
    @ParameterizedTest
    @ValueSource(ints = { 19, 99, 100 })
    void extractFare_adult(int age) {
        assertThat(AgeDiscountPolicy.discountFromAge(age, BASE_FARE)).isEqualTo(BASE_FARE);
    }
}