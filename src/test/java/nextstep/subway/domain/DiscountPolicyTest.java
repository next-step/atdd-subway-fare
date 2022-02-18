package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DiscountPolicyTest {

    @Test
    @DisplayName("로그인 하지 않는 사용자는 요금 할인이 적용되지 않는다.")
    void discountFareNonLoggedInUser() {
        int age = 1;
        int fare = 2_150;

        //when
        int result = DiscountPolicy.discount(false, age, fare);

        //then
        assertThat(result).isEqualTo(fare);
    }

    @ParameterizedTest(name = "로그인한 사용자의 경우 {0}살이 {1}원의 요금이 나왔을 때 실제로 내는 금액은 {2}원이다")
    @MethodSource("generateData")
    void discountFare(int age, int fare, int discountedFare) {
        //when
        int result = DiscountPolicy.discount(true, age, fare);

        //then
        assertThat(result).isEqualTo(discountedFare);
    }

    static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(0, 1000, 0),
                Arguments.of(6, 1000, 675),
                Arguments.of(7, 1000, 675),
                Arguments.of(12, 1000, 675),
                Arguments.of(13, 1000, 870),
                Arguments.of(14, 1000, 870),
                Arguments.of(18, 1000, 870),
                Arguments.of(19, 1000, 1000),
                Arguments.of(20, 1000, 1000)
        );
    }
}