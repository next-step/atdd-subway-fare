package nextstep.subway.unit;

import nextstep.subway.domain.discount.DiscountPolicy;
import nextstep.subway.domain.discount.NoDiscountPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NoDiscountPolicyTest {

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20})
    void noDiscountSupport(int age) {
        DiscountPolicy discountPolicy = new NoDiscountPolicy();
        int fare = 1000;

        assertAll(
                () -> assertThat(discountPolicy.support(age)).isTrue(),
                () -> assertThat(discountPolicy.discount(fare)).isEqualTo(fare)
        );
    }

}
