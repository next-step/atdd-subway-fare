package nextstep.subway.util;

import nextstep.subway.util.discount.Adult;
import nextstep.subway.util.discount.DiscountAgePolicy;
import nextstep.subway.util.discount.Children;
import nextstep.subway.util.discount.Teenager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserDiscountPolicyByPaymentAgeTestPolicy {


    @DisplayName("나이대별로 할인 되는 금액이 다르다.")
    @ParameterizedTest
    @MethodSource("variousAgeMember")
    void discountPolicyByAge(DiscountAgePolicy discountAgePolicy, int fare, int expectedFare) {
        DiscountPolicy discountPolicy = new UserDiscountPolicyByAge(discountAgePolicy);

        int discountedFare = discountPolicy.discount(fare);

        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> variousAgeMember() {
        return Stream.of(
                Arguments.of(new Children(), 1_250, 800),
                Arguments.of(new Teenager(), 1_250, 1_070),
                Arguments.of(new Adult(), 1_250, 1_250)
        );
    }
}