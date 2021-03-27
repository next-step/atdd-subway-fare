package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    @ParameterizedTest
    @DisplayName("요금 거리별 계산")
    @CsvSource(value = {
            "10,1250",
            "20,1450",
            "58,2150"
    })
    void calculate(int distance, int fare) {
        Fare actual = new Fare(distance);
        assertThat(actual.getFare()).isEqualTo(fare);
    }

    @DisplayName("거리가 0보다 작은 경우")
    @Test
    void validateDistanceLessThanZero() {
        assertThatThrownBy(() -> {new Fare(0); })
                .isInstanceOf(IllegalArgumentException.class);
    }

}
