package nextstep.api.unit.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.api.subway.domain.path.FareDiscounts;

class FareDiscountsTest {

    @ParameterizedTest
    @ValueSource(longs = {6, 12, 13, 18})
    void 청소년이거나_어린이여도_요금이공제금액보다_적거나_같은경우_요금할인_없다(final long age) {
        final var expectedFare = 350;
        final var actualFare = FareDiscounts.discountFareByAge(expectedFare, age);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(longs = {5})
    void 나이가_6세_미만인_경우_요금할인은_없다(final long age) {
        final var fare = 1350;
        final var expectedFare = 1350;

        final var actualFare = FareDiscounts.discountFareByAge(fare, age);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(longs = {6, 8, 12})
    void 나이가_6세_이상_13세_미만인_경우_공제한_금액의_50퍼센트_할인이_적용된다(final long age) {
        final var fare = 1350;
        final var expectedFare = 850;
        
        final var actualFare = FareDiscounts.discountFareByAge(fare, age);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(longs = {13, 15, 18})
    void 나이가_13세_이상_19세_미만인_경우_공제한_금액의_20퍼센트_할인이_적용된다(final long age) {
        final var fare = 1350;
        final var expectedFare = 1150;

        final var actualFare = FareDiscounts.discountFareByAge(fare, age);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(longs = {19})
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void 나이가_19세_이상인_경우_요금할인은_없다(final long age) {
        final var fare = 1350;
        final var expectedFare = 1350;

        final var actualFare = FareDiscounts.discountFareByAge(fare, age);
        assertThat(actualFare).isEqualTo(expectedFare);
    }
}