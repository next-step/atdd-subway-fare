package nextstep.subway.unit;

import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.MemberDiscountPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDiscountPolicyTest {

    @ParameterizedTest
    @CsvSource({"6, 0.5", "13, 0.2", "20, 0"})
    void decide(int age, double rate) {
        // when
        MemberDiscountPolicy policy = MemberDiscountPolicy.decide(age);

        // then
        assertThat(policy.getRate()).isEqualTo(rate);
    }

    @ParameterizedTest
    @CsvSource({"6, 1100", "13, 440", "20, 0"})
    void calculateOverFare(int age, int expectedOverFare) {
        // given
        int beforeDiscountFare = 2550;
        MemberDiscountPolicy policy = MemberDiscountPolicy.decide(age);

        // when
        int overFare = policy.calculateDiscountFare(beforeDiscountFare, Fare.BASIC_DEDUCTION_FARE);

        // then
        assertThat(overFare).isEqualTo(expectedOverFare);
    }
}