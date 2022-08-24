package nextstep.subway.util.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountPolicyTest {
    @DisplayName("청소년, 어린이가 아닌 경우 할인받지 않는다.")
    @Test
    void calculate() {
        assertThat(AgeDiscountPolicy.calculate(1250, 19)).isEqualTo(0);
    }

    @DisplayName("어린이는 요금의 80%를 할인받는다.")
    @CsvSource(value = {"6:720", "12:720"}, delimiter = ':')
    @ParameterizedTest
    void calculateChildrenFare(int age, int discount) {
        assertThat(AgeDiscountPolicy.calculate(1250, age)).isEqualTo(discount);
    }

    @DisplayName("청소년은 요금의 50%를 할인받는다.")
    @CsvSource(value = {"13:450", "18:450"}, delimiter = ':')
    @ParameterizedTest
    void calculateTeenagerFare(int age, int discount) {
        assertThat(AgeDiscountPolicy.calculate(1250, age)).isEqualTo(discount);
    }
}
