package nextstep.subway.maps.map.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareContextTest {

    private FareContext fareContext;

    @BeforeEach
    void setUp() {
        fareContext = new FareContext(0);
    }

    @Test
    void plusFare() {
        // when
        fareContext.plusFare(100);
        fareContext.plusFare(200);
        fareContext.plusFare(300);
        fareContext.plusFare(400);

        // then
        assertThat(fareContext.getFare()).isEqualTo(FareContext.DEFAULT_FARE + 1000);
    }

    @Test
    void discountFare() {
        // when
        fareContext.discountFare(100);
        fareContext.discountFare(200);
        fareContext.discountFare(300);
        fareContext.discountFare(400);

        // then
        assertThat(fareContext.getFare()).isEqualTo(FareContext.DEFAULT_FARE - 1000);
    }

    @ParameterizedTest
    @CsvSource({"55, 0.45", "30, 0.7", "10, 0.9"})
    void discountPercent(int percent, float expected) {
        // when
        fareContext.discountPercent(percent);

        // then
        assertThat(fareContext.getFare()).isEqualTo((int) Math.floor(FareContext.DEFAULT_FARE * expected));
    }
}