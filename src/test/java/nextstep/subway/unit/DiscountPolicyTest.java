package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.DiscountPolicy;

class DiscountPolicyTest {

    @DisplayName("성인은 요금이 할인되지 않는다.")
    @Test
    void adult() {
        // when & then
        assertThat(DiscountPolicy.discount(1_250, 20)).isEqualTo(1_250);
    }

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20%가 할인된다.")
    @ValueSource(ints = {13, 18})
    @ParameterizedTest
    void teenager(int age) {
        // when & then
        assertThat(DiscountPolicy.discount(1_250, age)).isEqualTo(1_070);
    }

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%가 할인된다.")
    @ValueSource(ints = {6, 12})
    @ParameterizedTest
    void child(int age) {
        // when & then
        assertThat(DiscountPolicy.discount(1_250, age)).isEqualTo(800);
    }
}
