package nextstep.subway.unit;

import nextstep.subway.domain.DiscountType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTypeTest {

    @ParameterizedTest(name = "할인 정책 조회 [{arguments}]")
    @MethodSource
    void constructor(int age, DiscountType expected) {
        //when
        DiscountType discountType = DiscountType.from(age);

        //then
        assertThat(discountType).isEqualTo(expected);
    }

    private static Stream<Arguments> constructor() {
        return Stream.of(
                Arguments.of(10, DiscountType.CHILD),
                Arguments.of(15, DiscountType.YOUTH),
                Arguments.of(20, DiscountType.ADULT)
        );
    }

    @ParameterizedTest(name = "연령대별 할인 금액 [{arguments}]")
    @CsvSource(value = {
            "10, 825",
            "15, 330",
            "20, 0"
    })
    void discount(int age, int expected) {
        //given
        DiscountType discountType = DiscountType.from(age);

        //when
        int fare = discountType.discountFare(2_000);

        //then
        assertThat(fare).isEqualTo(expected);

    }
}
