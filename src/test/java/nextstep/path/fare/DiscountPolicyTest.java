package nextstep.path.fare;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountPolicyTest {

    @DisplayName("나이에 따라 요금을 할인한다")
    @ParameterizedTest
    @CsvSource({"6, 450", "13, 720", "19, 1250"})
    void discount(int age, int expected) {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        Fare fare = discountPolicy.discount(Fare.DEFAULT_FARE, age);
        assertThat(fare).isEqualTo(Fare.of(expected));
    }
}