package nextstep.subway.applicaion.discount;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        final boolean result = target.isTarget(member(age));

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 10, 11, 12, 19, 20, 21})
    void 할인대상이아님(final int age) {
        final boolean result = target.isTarget(member(age));

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"1550, 960", "2150, 1440"})
    void 할인금액계산(final int before, final int after) {
        final int result = target.discount(before);

        assertThat(result).isEqualTo(after);
    }

    private Member member(final int age) {
        return new Member(null, null, age);
    }
}