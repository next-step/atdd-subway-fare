package nextstep.subway.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountAgePolicyTest {

    @DisplayName("나이의 경계 값 테스트")
    @ParameterizedTest
    @MethodSource("ageBoundary")
    void ageBoundaryTest(int userAge, DiscountAgePolicy discountAgePolicy) {
        DiscountAgePolicy findUserDiscountAgePolicy = DiscountAgeFactory.findUsersAge(userAge);

        assertThat(findUserDiscountAgePolicy).isExactlyInstanceOf(discountAgePolicy.getClass());
    }

    private static Stream<Arguments> ageBoundary() {
        return Stream.of(
                Arguments.of(6, new DiscountChildren()),
                Arguments.of(12, new DiscountChildren()),
                Arguments.of(13, new DiscountTeenager()),
                Arguments.of(18, new DiscountTeenager()),
                Arguments.of(19, new DiscountAdult())
        );
    }
}