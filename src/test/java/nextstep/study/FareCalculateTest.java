package nextstep.study;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculateTest {

    @ParameterizedTest
    @CsvSource(value = {"1,100","6,200","15,300","42,900"}, delimiterString = ",")
    void 거리대비요금계산(int distance, int fare) {
        assertThat((int) ((Math.ceil((distance - 1) / 5) + 1) * 100)).isEqualTo(fare);
    }
}
