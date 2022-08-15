package nextstep.subway.applicaion.discount;

import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ChildRateDiscountPolicyTest {

    private ChildRateDiscountPolicy target;

    @BeforeEach
    void setUp() {
        target = new ChildRateDiscountPolicy();
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void 할인대상임(final int age) {
        final boolean result = target.isTarget(member(age));

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 13, 14, 15, 16})
    void 할인대상이아님(final int age) {
        final boolean result = target.isTarget(member(age));

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"1550, 600", "2150, 900"})
    void 할인금액계산(final int before, final int after) {
        final int result = target.discount(before);

        assertThat(result).isEqualTo(after);
    }

    private Member member(final int age) {
        return new Member(null, null, age);
    }

}