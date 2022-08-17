package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.discount.AdultDiscountPolicy;
import nextstep.subway.price.discount.DiscountPolicyCalculate;
import nextstep.subway.price.discount.TeenagerDiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TeenagerDiscountPolicyTest {

    private DiscountPolicyCalculate calculate;

    @BeforeEach
    void setup() {
        calculate = new TeenagerDiscountPolicy();
    }

    @DisplayName("13세 이상 19세 미만, 할인 정책이 들어가는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void conditionTest(int age) {
        assertThat(calculate.condition(age)).isTrue();
    }

    @DisplayName("13세 이상 19세 미만 미만일 경우 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 19, 20})
    void returnFalseOfCondition(int age) {

        assertThat(calculate.condition(age)).isFalse();
    }

    @DisplayName("13세 이상 19세 미만 계산 정책 적용")
    @ParameterizedTest
    @ValueSource(ints = {100, 500, 1000, 1500})
    void childPolicyOfDiscount(int price) {

        assertThat(calculate.discount(price)).isEqualTo(calculate(price));
    }

    private int calculate(int price) {
        return (int) ((price - 350) * 0.8);
    }

}
