package nextstep.subway.maps.fare.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    private Fare fare;

    @BeforeEach
    void setUp() {
        fare = new Fare(1000);
    }

    @DisplayName("요금을 추가한다")
    @Test
    void plusFare() {
        // when
        fare.plusFare(100);
        fare.plusFare(200);
        fare.plusFare(300);
        fare.plusFare(400);

        // then
        assertThat(fare.getValue()).isEqualTo(2000);
    }

    @DisplayName("요금을 할인한다")
    @Test
    void discountFare() {
        // when
        fare.discountFare(100);
        fare.discountFare(200);
        fare.discountFare(300);
        fare.discountFare(400);

        // then
        assertThat(fare.getValue()).isEqualTo(0);
    }

    @DisplayName("요금을 퍼센트 할인한다")
    @ParameterizedTest
    @CsvSource({"55, 0.45", "30, 0.7", "10, 0.9", "100, 0"})
    void discountPercent(int percent, float expected) {
        // when
        fare.discountPercent(percent);

        // then
        assertThat(fare.getValue()).isEqualTo((int) Math.floor(1000 * expected));
    }
}
