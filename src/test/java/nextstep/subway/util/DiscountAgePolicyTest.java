package nextstep.subway.util;

import nextstep.subway.domain.Fare;
import nextstep.subway.util.discount.Adult;
import nextstep.subway.util.discount.AgeFactory;
import nextstep.subway.util.discount.DiscountAgePolicy;
import nextstep.subway.util.discount.Children;
import nextstep.subway.util.discount.Teenager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DiscountAgePolicyTest {

    @Autowired
    private AgeFactory ageFactory;

    @DisplayName("나이의 경계 값 테스트")
    @ParameterizedTest
    @MethodSource("ageBoundary")
    void ageBoundaryTest(int userAge, DiscountAgePolicy discountAgePolicy) {
        DiscountAgePolicy findUserDiscountAgePolicy = ageFactory.findUsersAge(userAge);

        assertThat(findUserDiscountAgePolicy).isExactlyInstanceOf(discountAgePolicy.getClass());
    }

    @DisplayName("나이대별로 할인 되는 금액이 다르다.")
    @ParameterizedTest
    @MethodSource("variousAgeMember")
    void discountPolicyByAge(DiscountAgePolicy discountAgePolicy, int fare, int expectedFare) {
        Fare resultFare = Fare.from(fare);
        discountAgePolicy.discount(resultFare);

        assertThat(resultFare.fare()).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> ageBoundary() {
        return Stream.of(
                Arguments.of(6, new Children()),
                Arguments.of(12, new Children()),
                Arguments.of(13, new Teenager()),
                Arguments.of(18, new Teenager()),
                Arguments.of(19, new Adult())
        );
    }

    private static Stream<Arguments> variousAgeMember() {
        return Stream.of(
                Arguments.of(new Children(), 1_250, 800),
                Arguments.of(new Teenager(), 1_250, 1_070),
                Arguments.of(new Adult(), 1_250, 1_250)
        );
    }
}