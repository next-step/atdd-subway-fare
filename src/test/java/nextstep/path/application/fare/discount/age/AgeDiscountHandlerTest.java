package nextstep.path.application.fare.discount.age;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountHandlerTest {

    @ParameterizedTest
    @CsvSource(value =
            {
                    "6, 500",
                    "13, 200",
                    "19, 0",
            }, delimiterString = ",")
    @DisplayName("연령별 요금을 할인한 금액을 반환받을 수 있다.")
    void ageDiscountTest(final int age, final long expectedDiscountFare) {
        final AgeDiscountHandler ageDiscountHandler =
                new KidDiscountHandler()
                        .next(new TeenDiscountHandler());
        final long baseFare = 1350L;

        final long discounted = ageDiscountHandler.discount(baseFare, age);

        assertThat(discounted).isEqualTo(baseFare - expectedDiscountFare);
    }
}
