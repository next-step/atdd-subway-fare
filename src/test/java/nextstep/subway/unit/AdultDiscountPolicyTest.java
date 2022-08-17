package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.price.discount.AdultDiscountPolicy;
import nextstep.subway.price.discount.ChildDiscountPolicy;
import nextstep.subway.price.discount.DiscountPolicyCalculate;
import nextstep.subway.price.discount.TeenagerDiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AdultDiscountPolicyTest {

    private DiscountPolicyCalculate calculate;

    @BeforeEach
    void setup() {
        calculate = new AdultDiscountPolicy();
    }

    @DisplayName("19세 이상 정책에 포함되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 21, 22, 23})
    void condition(int age) {
        assertThat(calculate.condition(age)).isTrue();
    }

    @DisplayName("19세 이상이 아닐경우 false 반환")
    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10, 15, 18})
    void returnFalseOfCondition(int age) {

        assertThat(calculate.condition(age)).isFalse();
    }

    @DisplayName("19세 이상 할인 정책 적용")
    @ParameterizedTest
    @ValueSource(ints = {100, 500, 1000, 1500, 2000})
    void discount(int price) {

        assertThat(calculate.discount(price)).isEqualTo(price);
    }

}
