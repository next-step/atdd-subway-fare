package nextstep.subway.domain.discount;

import nextstep.subway.domain.LoginUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TeenageRateDiscountPolicyTest {

    private TeenageRateDiscountPolicy target;

    @BeforeEach
    void setUp() {
        target = new TeenageRateDiscountPolicy();
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void 할인대상임(final int age) {
        final boolean result = this.target.isTarget(loginUser(age));

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 10, 11, 12, 19, 20, 21})
    void 할인대상이아님(final int age) {
        final boolean result = this.target.isTarget(loginUser(age));

        assertThat(result).isFalse();
    }

    private LoginUser loginUser(final int age) {
        return new LoginUser(null, null, age, null);
    }

}