package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.discount.ChildDiscountPolicy;
import nextstep.subway.price.discount.DiscountPolicyCalculate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ChildDiscountPolicyTest {

    private DiscountPolicyCalculate calculate;

    @BeforeEach
    void setup() {
        calculate = new ChildDiscountPolicy();
    }

    @DisplayName("6세 이상, 13세 미만 계산이 들어가는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void conditionTest(int age) {

        assertThat(calculate.condition(age)).isTrue();
    }

    @DisplayName("6세 이상, 13세 미만일 경우 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 13, 14, 15})
    void returnFalseOfCondition(int age) {

        assertThat(calculate.condition(age)).isFalse();
    }

    @DisplayName("6세 이상, 13세 미만 계산 정책 적용")
    @ParameterizedTest
    @ValueSource(ints = {100, 500, 1000, 1500})
    void childPolicyOfDiscount(int price) {

        assertThat(calculate.discount(price)).isEqualTo(calculate(price));
    }

    private int calculate(int price) {
        return (int) ((price - 350) * 0.5);
    }

}
