package nextstep.api.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareSectionsTest {

    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,1250", "10,1250"})
    void 거리가_10km_이하인_경우(final long distance, final long expectedFare) {
        final var actualFare = FareSections.calculateTotalFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "50,2050"})
    void 거리가_10km_초과_50km_이하인_경우(final long distance, final long expectedFare) {
        final var actualFare = FareSections.calculateTotalFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void 거리가_50km_초과인_경우(final long distance, final long expectedFare) {
        final var actualFare = FareSections.calculateTotalFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

}